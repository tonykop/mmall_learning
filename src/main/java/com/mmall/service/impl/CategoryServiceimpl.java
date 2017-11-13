package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by tony on 2017/10
 */
@Service("iCategoryService")
public class CategoryServiceimpl implements ICategoryService{

    private Logger logger = LoggerFactory.getLogger(CategoryServiceimpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse addCategory(String categoryName,Integer parentId){
        if(parentId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("add category error!");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//this category can use.
        int rowCount = categoryMapper.insert(category);
        if(rowCount > 0){
           return ServerResponse.createBySuccess("add category success!");
        }
        return ServerResponse.createByErrorMessage("add category faild!");
    }

    public ServerResponse updateCategoryName(Integer categoryId,String categoryName){
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("updapate category name error!");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKey(category);
        if(rowCount >0){
            return ServerResponse.createBySuccess("update category name success!");
        }
        return ServerResponse.createByErrorMessage("update category name faild!");
    }

    public ServerResponse<List<Category>> getChildrenParalleCategory(Integer categoryId){
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
               logger.info("can not find now category children category!");
        }

        return ServerResponse.createBySuccess(categoryList);
    }

    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategoty(categorySet,categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null){
           for(Category categoryItem : categorySet){
              categoryIdList.add(categoryItem.getId());
           }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    //di gui sun fa sun chu zi jie dian
    private Set<Category> findChildCategoty(Set<Category> categorySet,Integer categoryId){
     Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
           categorySet.add(category);
        }

        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for(Category categoryItem : categoryList){
           findChildCategoty(categorySet,categoryItem.getId());
        }

        return  categorySet;
    }
}