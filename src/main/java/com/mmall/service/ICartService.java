package com.mmall.service;

import com.mmall.vo.CartVo;
import com.mmall.vo.ServerResponse;

/**
 * ${DESCRIPTION}
 *
 * @author Administrator
 * @create 2017-07-27-20:08
 */
public interface ICartService{

    //加入购物车
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    //更新购物车
    ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count);

    //删除购物车
    ServerResponse<CartVo> deleteProduct(Integer userId,String productIds);

    //购物车列表
    ServerResponse<CartVo> list(Integer userId);

    ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer productId,Integer checked);

    ServerResponse<Integer> getCartProductCount(Integer userId);
}
