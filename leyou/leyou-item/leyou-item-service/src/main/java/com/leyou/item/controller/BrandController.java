package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xiang
 * @date 2020/10/26
 */
@Controller
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * http://api.leyou.com/api/item/brand/page?key=&page=1&rows=5&sortBy=id&desc=false
     * params: {
     *             key: this.search, // 搜索条件
     *             page: this.pagination.page,// 当前页
     *             rows: this.pagination.rowsPerPage,// 每页大小
     *             sortBy: this.pagination.sortBy,// 排序字段
     *             desc: this.pagination.descending// 是否降序
     *         }
     */

    /**
     * 根据查询条件分页并排序查询商品的品牌
     *
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> searchBrands(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", required = false) Boolean desc
    ) {
        PageResult<Brand> searchBrands = brandService.searchBrands(key, page, rows, sortBy, desc);
        if (CollectionUtils.isEmpty(searchBrands.getItems())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(searchBrands);
    }

    /**
     * 新增品牌
     *
     * @param brand
     * @param cids
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        this.brandService.saveBrand(brand, cids);
        // 不报异常即是成功
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据商品分类获取品牌
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> listBrandsByCid(@PathVariable("cid") Long cid) {
        List<Brand> listBrands = brandService.listBrandsByCid(cid);

        if (CollectionUtils.isEmpty(listBrands)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(listBrands);
    }

    @GetMapping("{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable("id")Long id){
        Brand brand = this.brandService.getBrandById(id);
        if(brand==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brand);
    }
}
