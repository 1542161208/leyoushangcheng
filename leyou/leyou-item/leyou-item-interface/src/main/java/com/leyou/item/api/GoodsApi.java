package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @description 商品相关接口
 * @author xiang
 * @create 2021/05/02
 */
public interface GoodsApi {

    // this.$http.loadData("/item/spu/detail/" + oldGoods.id)
    @GetMapping("spu/detail/{spuId}")
    public SpuDetail getSpuDetailBySpuId(@PathVariable("spuId") Long spuId);

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
    public PageResult<SpuBo> searchSpuByPage(
        @RequestParam(value = "key", required = false) String key,
        @RequestParam(value = "saleable", required = false) Boolean saleable,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "rows", defaultValue = "5") Integer rows
    );

    /**
     * 根据spuId查询Sku列表
     * @param spuId
     * @return
     */
    // this.$http.loadData("/item/sku/list?id=" + oldGoods.id)
    @GetMapping("sku/list")
    public List<Sku> listSkusBySpuId(@RequestParam("id")Long spuId);

    /**
     * 根据商品ID查询Spu
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Spu getSpuById(@PathVariable("id") Long id);

    @GetMapping("sku/{id}")
    public Sku querySkuById(@PathVariable("id")Long id);
}
