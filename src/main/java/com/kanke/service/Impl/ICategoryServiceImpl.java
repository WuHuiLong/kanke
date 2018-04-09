package com.kanke.service.Impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kanke.commom.ServerResponse;
import com.kanke.dao.CategoryMapper;
import com.kanke.pojo.Category;
import com.kanke.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class ICategoryServiceImpl implements ICategoryService{
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName,Integer parentId){
        if(parentId==null|| StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMsg("参数错误");
        }
        Category category=new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//这个分类是可用的

        int rowCount=categoryMapper.insert(category);
        if(rowCount>0){
            return  ServerResponse.createBySuccess("添加品类成功");
        }
        return  ServerResponse.createByErrorMsg("添加品类失败");
    }

    public ServerResponse updateCategoryName(Integer categoryId,String categoryName){
        if(categoryId==null|| StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMsg("参数错误");
        }
        Category category=new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount=categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount>0){
            return ServerResponse.createBySuccess("更新分类名称成功");
        }
        return ServerResponse.createByErrorMsg("更新分类名称失败");
    }

    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
        List<Category> list=categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(list)){
            return ServerResponse.createByErrorMsg("未找到当前集合的子类");
        }
        return ServerResponse.createBySuccess(list);
    }

    public ServerResponse<List<Integer>> selectCategoryAndChildById(Integer categoryId){
        Set<Category> categorySet = Sets.newHashSet();
        findChildrenCategory(categorySet,categoryId);

        List<Integer> categoryIdList= Lists.newArrayList();
        if(categoryId !=null){
            for(Category categoryItem:categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    //递归
    public Set<Category> findChildrenCategory(Set<Category> categorySet, Integer categoryId){
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            categorySet.add(category);
        }
        //查找子结点
        List<Category> categoryList=categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem:categoryList) {
            findChildrenCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }
}
