package com.leyou.item.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author xiang
 * @date 2020/10/3
 */
@RequestMapping("category")
public interface CategoryApi {
    /**
     * 根据分类id集合获取分类名称集合
     */
    @GetMapping
    public List<String> listNamesByIds(@RequestParam("ids") List<Long> ids);
}
