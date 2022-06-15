package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lx
 * @date 2020/10/3
 */
@Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * @RequestParam:传递的是字符串类型,直接用Long类型接收了？这个注解难道可以自动转换？
     */
    @RequestMapping("list")
    public ResponseEntity<List<Category>> listCategorys(@RequestParam(value = "pid", defaultValue = "0") Long pid) {

        if (pid == null || pid < 0) {
            // 400:参数不合法
            return ResponseEntity.badRequest().build();
        }
        List<Category> categories = categoryService.listCategorys(pid);
        System.out.println(categories);
        if (CollectionUtils.isEmpty(categories)) {
            // 500：资源服务器找不到
            return ResponseEntity.notFound().build();
        }
        // 200：响应成功
        return ResponseEntity.ok(categories);
    }

    /**
     * 根据分类id集合获取分类名称集合
     */
    @GetMapping
    public ResponseEntity<List<String>> listNamesByIds(@RequestParam("ids") List<Long> ids) {
        List<String> names = this.categoryService.listNamesByIds(ids);
        if (CollectionUtils.isEmpty(names)) {
            // 500：资源服务器找不到
            return ResponseEntity.notFound().build();
        }
        // 200：响应成功
        return ResponseEntity.ok(names);
    }
}
