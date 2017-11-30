package com.akk.service.impl;

import com.akk.converter.OrderMasterZOrderDTOConverter;
import com.akk.dataobject.OrderDetail;
import com.akk.dataobject.OrderMaster;
import com.akk.dataobject.ProductInfo;
import com.akk.dto.CartDTO;
import com.akk.dto.OrderDTO;
import com.akk.enums.OrderStatusEnum;
import com.akk.enums.PayStatusEnum;
import com.akk.enums.ResultEnum;
import com.akk.exception.SellException;
import com.akk.repository.OrderDetailRepository;
import com.akk.repository.OrderMasterRepository;
import com.akk.service.OrderService;
import com.akk.service.PayService;
import com.akk.service.ProductService;
import com.akk.service.WebSocket;
import com.akk.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by KHM
 * 2017/7/27 11:28
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private PayService payService;

    @Autowired
    private WebSocket webSocket;

    @Override
    @Transactional//事务管理，一旦失败就回滚
    public OrderDTO create(OrderDTO orderDTO) {
        //设置下订单id(是个随机，这里调用了根据时间产生6位随机数的方法)
        String orderId = KeyUtil.genUniqueKey();
        //给总价赋值
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);

        //List<CartDTO> cartDTOList = new ArrayList<>();

        //1.查询商品(数量，价格)
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()){
            //System.out.println("这是前端传过来商品id："+orderDetail.getProductId());
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
                if(productInfo == null){
                    throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
                }
            //System.out.println("根据前端id查询出来的商品信息："+ productInfo.getProductId()+"   "+productInfo.getProductName());
            //2.计算总价=单价*数量+orderAmount
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);

            //3.订单详情入库(OrderMaster和orderDetail)
            //利用BeanUtils方法把前端查找出来的productInfo商品信息复制给订单详情
            BeanUtils.copyProperties(productInfo, orderDetail);//先复制，再赋值
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);

            orderDetailRepository.save(orderDetail);

           /* CartDTO cartDTO = new CartDTO(orderDetail.getProductId(), orderDetail.getProductQuantity());
            cartDTOList.add(cartDTO);*/
        }

        //4.订单总表入库(OrderMaster和orderDetail)
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmount);//是一个整个订单的总价，所以在foe循环之外设置
        orderMaster.setOrderStatus(OrderStatusEnum.New.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);

        //5.扣库存
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductQuantity())
        ).collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);

        //发送websocket消息
        webSocket.sendMessage(orderDTO.getOrderId());

        return orderDTO;
    }

    @Override//查询订单详情
    public OrderDTO findOne(String orderId) {

        OrderMaster orderMaster = orderMasterRepository.findOne(orderId);
        if(orderMaster == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //这种情况是总订单有但订单详情没有，一般不会出现此问题
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        //1.先从数据库中查找总订单列表
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        //这里就不用判断了，上面的判断相当于事务，不能只查出一半东西,而这里没查出来东西也正常

        //2.再把实体转换成要返回的格式
        List<OrderDTO> orderDTOList = OrderMasterZOrderDTOConverter.convert(orderMasterPage.getContent());
        //PageImpl是生成一个page对象的构造方法
        return new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();

        //1.判断订单状态
        //只有订单状态为新订单时才可以取消
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.New.getCode())){
            /*  用日志来记录下，有可能出错的情况 */
            log.error("[取消订单] 订单状态不正确， orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //2.修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if(updateResult == null){
            log.error("[取消订单] 更新失败， orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        //3.返回库存
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("[取消订单] 订单中无商品信息， orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }//如果有商品就要增加库存
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.increaseStock(cartDTOList);

        //? 删除订单详情信息不需要，取消了还是能查得到

        //4.如果已支付，需要退款
        if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
            payService.refund(orderDTO);
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        //1.判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.New.getCode())){
            log.error("[完结订单] 订单状态不正确，orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //2.修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if(updateResult == null){
            log.error("[完结订单] 更新失败， orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        //1.判断订单状态（还是判断是不是新订单）
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.New.getCode())){
            log.error("[订单支付完成] 订单状态不正确，orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //2.判断支付状态
        if(!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
            log.error("[订单支付完成] 订单支付状态不正确，orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        //3.修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if(updateResult == null){
            log.error("[订单支付成功] 更新失败， orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findAll(pageable);

        //2.再把实体转换成要返回的格式
        List<OrderDTO> orderDTOList = OrderMasterZOrderDTOConverter.convert(orderMasterPage.getContent());
        //PageImpl是生成一个page对象的构造方法
        return new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }
}
