package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ServerResponse;

/**
 * ${DESCRIPTION}
 *
 * @author Administrator
 * @create 2017-07-25-18:25
 */
public interface IProductService {

    //新增产品
    ServerResponse saveOrUpdateProduct(Product product);

    //产品上下架
    ServerResponse<String> setSaleStatus(Integer productId,Integer status);

    //获取商品详情
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    //商品详情分页列表
    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    //根据产品id和产品名称搜索产品列表并分页
    ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);

    //前台根据商品id获取上架的商品详情
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);


    //根据关键字和分类id搜索
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
