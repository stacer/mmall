package com.mmall.service;

import com.mmall.pojo.Category;
import com.mmall.vo.ServerResponse;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author Administrator
 * @create 2017-07-25-16:49
 */
public interface ICategoryService {

    //添加品类
    ServerResponse addCategory(String categoryName, Integer parentId);

    //更新品类
    ServerResponse updateCategoryName(Integer categoryId,String categoryName);

    //根据品类id查找同级品类
    ServerResponse<List<Category>> getChildrenParalleCategory(Integer categoryId);

    //递归查询所有子节点
    ServerResponse selectCategoryAndChildrenById(Integer categoryId);
}
