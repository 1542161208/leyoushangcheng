package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xiang
 * @description 商品处理接口
 * @date 2020/12/26
 */
@Controller
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    /**
     * 根据条件分页查询spu
     *
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return PageResult<SpuBo>
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> searchSpuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows

    ) {
        PageResult<SpuBo> pageResult = this.goodsService.searchSpuByPage(key, saleable, page, rows);
        if (CollectionUtils.isEmpty(pageResult.getItems())) {
            // 先校验非法的阻断
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 新增商品
     *
     * @param spuBo
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo) {
        goodsService.saveGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 更新商品:更新和删除成功没有返回值,返回204即noContent
     *
     * @param spuBo
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo) {
        this.goodsService.updateGoods(spuBo);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据spuId查询SpuDetail
     *
     * @param spuId
     * @return
     */
    // this.$http.loadData("/item/spu/detail/" + oldGoods.id)
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> getSpuDetailBySpuId(@PathVariable("spuId") Long spuId) {
        SpuDetail spuDetail = this.goodsService.getSpuDetailBySpuId(spuId);
        if (spuDetail == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 根据spuId查询Sku列表
     *
     * @param spuId
     * @return
     */
    // this.$http.loadData("/item/sku/list?id=" + oldGoods.id)
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> listSkusBySpuId(@RequestParam("id") Long spuId) {
        List<Sku> listSkus = this.goodsService.listSkusBySpuId(spuId);
        if (CollectionUtils.isEmpty(listSkus)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(listSkus);
    }

    /**
     * 根据商品ID查询Spu
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Spu> getSpuById(@PathVariable("id") Long id) {
        Spu spu = this.goodsService.getSpuById(id);
        if (spu == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(spu);
    }

    @GetMapping("sku/{id}")
    public ResponseEntity<Sku> querySkuById(@PathVariable("id")Long id){
        Sku sku = this.goodsService.querySkuById(id);
        if (sku == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(sku);
    }
}
