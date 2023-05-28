package com.leyou.goods.service;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xiang
 * @description 商品详情服务
 * @date 2021/10/03
 */
@Service
public class GoodsService {
    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    public Map<String, Object> loadData(Long spuId) {
        Map<String, Object> model = new HashMap<>();
        // 1.根据商品ID查询商品
        Spu spu = this.goodsClient.getSpuById(spuId);

        // 2.根据商品ID查询商品明细
        SpuDetail spuDetail = this.goodsClient.getSpuDetailBySpuId(spuId);

        // 3.查询分类:Map<String,Object>
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.listNamesByIds(cids);
        // 初始化一个分类的List
        List<Map<String, Object>> categories = new ArrayList<>();
        for (int i = 0; i < cids.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", cids.get(i));
            map.put("name", names.get(i));
            categories.add(map);
        }

        // 4.查询品牌
        Brand brand = this.brandClient.getBrandById(spu.getBrandId());

        // 5.查询skus
        List<Sku> skus = this.goodsClient.listSkusBySpuId(spuId);

        // 6.查询规格参数组
        List<SpecGroup> specGroups = this.specificationClient.listSpecGroupWithParam(spu.getCid3());

        // 7.查询特殊规格参数
        List<SpecParam> specParams = this.specificationClient.listSpecParams(null, spu.getCid3(), false, null);
        // 初始化特殊规格参数的Map
        Map<Long, String> paramMap = new HashMap<>();
        specParams.forEach(p -> {
            paramMap.put(p.getId(), p.getName());
        });

        model.put("spu", spu);
        model.put("spuDetail", spuDetail);
        model.put("categories", categories);
        model.put("brand", brand);
        model.put("skus", skus);
        model.put("groups", specGroups);
        model.put("paramMap", paramMap);

        return model;
    }
}
