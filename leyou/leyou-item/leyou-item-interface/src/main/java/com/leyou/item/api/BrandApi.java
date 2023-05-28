package com.leyou.item.api;

import com.leyou.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @author xiang
 * @date 2020/10/26
 */
@RequestMapping("brand")
public interface BrandApi {
    @GetMapping("{id}")
    public Brand getBrandById(@PathVariable("id")Long id);
}
