<html>
<#--head公用-->
<#include "../common/header.ftl">
<body>

<div id="wrapper" class="toggled">

<#--边栏siderbae-->
<#include "../common/nav.ftl">

<#--主要内容content-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <form role="form" method="post" action="/sell/seller/product/save">

                        <div class="form-group">
                            <label>名称</label>
                            <#--这里也可以为空，但是是对象就用单引号-->
                            <input name="productName" type="text" class="form-control" value="${(productInfo.productName)!''}" />
                        </div>
                        <div class="form-group">
                            <label>价格</label>
                            <input name="productPrice" type="text" class="form-control" value="${(productInfo.productPrice)!''}"/>
                        </div>
                        <div class="form-group">
                            <label>库存</label>
                            <input name="productStock" type="number" class="form-control" value="${(productInfo.productStock)!''}"/>
                        </div>
                        <div class="form-group">
                            <label>描述</label>
                            <input name="productDescription" type="text" class="form-control" value="${(productInfo.productDescription)!''}"/>
                        </div>
                        <div class="form-group">
                            <label>图片</label>
                            <img height="100" width="100" src="${(productInfo.productIcon)!''}" alt="">
                            <input name="productIcon" type="text" class="form-control" value="${(productInfo.productIcon)!''}"/>
                        </div>
                        <div class="form-group">
                            <label>类目</label>
                            <select name="categoryType" class="from-control">
                                <#list categoryList as category>
                                    <option value="${category.categoryType}"
                                        <#--但现在这样查出来是按数据库的排列显示的，我们要做个判断让他根据所选商品来显示-->
                                        <#if (productInfo.categoryType)?? && productInfo.categoryType == category.categoryType>
                                        <#--这一列就是被选中的状态-->
                                        selected
                                        </#if>
                                        >${category.categoryName}
                                    </option>
                                </#list>
                            </select>
                        </div>
                        <input hidden type="text" name="productId" value="${(productInfo.productId)!''}">
                        <button type="submit" class="btn btn-default">提交</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>

</body>
</html>