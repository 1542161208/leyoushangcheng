package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lx
 * @date 2020/10/3
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> listCategorys(Long pid) {
        Category example = new Category();
        example.setParentId(pid);
        List<Category> categories = categoryMapper.select(example);
        if (!CollectionUtils.isEmpty(categories)) {
            return categories;
        } else {
            return new ArrayList<Category>();
        }
    }

    public List<String> listNamesByIds(List<Long> ids) {
        List<Category> listCategorys = categoryMapper.selectByIdList(ids);
        // return listCategorys.stream().map(category -> {return category.getName();}).collect(Collectors.toList());
        return listCategorys.stream().map(category -> category.getName()).collect(Collectors.toList());
    }
}
