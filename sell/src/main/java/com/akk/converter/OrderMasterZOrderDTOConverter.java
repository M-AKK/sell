package com.akk.converter;

import com.akk.dataobject.OrderMaster;
import com.akk.dto.OrderDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 顾名思义，OrderMaster类转换成OrderDTO类的转换工具
 * Created by KHM
 * 2017/7/29 16:53
 */
public class OrderMasterZOrderDTOConverter {

    public static OrderDTO convert(OrderMaster orderMaster){
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        return orderDTO;
    }
    //把一个List中每一个实体对象转换成另一个实体
    public static List<OrderDTO> convert(List<OrderMaster> orderMasterList){
        return orderMasterList.stream().map(e ->
                convert(e)
        ).collect(Collectors.toList());
    }
}
