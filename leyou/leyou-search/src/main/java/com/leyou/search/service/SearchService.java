package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lx
 * @description 搜索微服务业务处理类
 * @date 2021/05/02
 */
@Service
public class SearchService {
    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private GoodsRepository goodsRepository;

    public Goods buildGoods(Spu spu) throws IOException {
        Goods goods = new Goods();

        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());

        // 根据分类的id查询分类名称
        List<String> names = this.categoryClient.listNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        // 根据品牌id查询品牌
        Brand brand = this.brandClient.getBrandById(spu.getBrandId());
        // 拼接all字段需要分类名称以及品牌名称
        goods.setAll(spu.getTitle() + brand.getName() + StringUtils.join(names, " "));

        // 根据spuId查询所有的sku
        List<Sku> skus = this.goodsClient.listSkusBySpuId(spu.getId());

        // 初始化一个价格集合收集所有sku的价格
        List<Long> prices = new ArrayList<>();

        // 收集sku的必要字段信息
        List<Map<String, Object>> skuMapList = new ArrayList<>();

        skus.forEach(sku -> {
            prices.add(sku.getPrice());

            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            // 获取sku中的图片,数据库中的图片可能是多张,所以以逗号切割获取第一张
            map.put("image", StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            skuMapList.add(map);
        });

        // 设置获取到spu下的所有sku的价格
        goods.setPrice(prices);

        // 获取sku下的所有sku并转化为json字符串(序列化)
        // Java对象转化成json字符串,这里就用到了jackon的jar包,使用writeValuesAsString的方法就可以把对角转化成json字符串
        goods.setSkus(MAPPER.writeValueAsString(skuMapList));

        // 根据spu中的cid3查询出所有的搜索规格参数
        List<SpecParam> params = this.specificationClient.listSpecParams(null, spu.getCid3(), null, true);

        // 获取所有查询的规格参数{规格参数名:根据规格参数名聚合得到的规格参数值}
        // 根据spuId查询spuDetail
        SpuDetail spuDetail = this.goodsClient.getSpuDetailBySpuId(spu.getId());

        // 把通用的规格参数值进行反序列化(反序列化)
        Map<String, Object> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<String, Object>>() {
        });

        // 把特殊的规格参数值进行反序列化(反序列化)
        Map<String, List<Object>> specialSpecMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List<Object>>>() {
        });

        Map<String, Object> specs = new HashMap<>();
        params.forEach(param -> {
            // 判断规格参数的类型是否是通用的规格参数
            if (param.getGeneric()) {
                // 通用的genericSpecMap中获取
                String value = genericSpecMap.get(param.getId().toString()).toString();
                if (param.getNumeric()) {
                    // 如果value的值是数字应该获取所在的区间
                    value = chooseSegment(value, param);
                }
                specs.put(param.getName(), value);
            } else {
                // 特殊的specialSpecMap中获取
                List<Object> value = specialSpecMap.get(param.getId().toString());
                specs.put(param.getName(), value);
            }
        });
        goods.setSpecs(specs);

        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        String result = "其它";

        if (StringUtils.isEmpty(value) || p == null) {
            return result;
        }

        double val = NumberUtils.toDouble(value);

        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    /**
     * 前台搜索功能
     *
     * @param searchRequest
     * @return
     */
    public SearchResult searchGoodsByPage(SearchRequest searchRequest) {
        // 入参判空
        if (StringUtils.isEmpty(searchRequest.getKey())) {
            return null;
        }

        // 自定义查询构建器
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        // 添加查询条件
        // QueryBuilder basicQuery = QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND);
        // nativeSearchQueryBuilder.withQuery(basicQuery);

        /**
         * 构建布尔查询
         * @param searchRequest
         * @return BoolQueryBuilder
         */
        BoolQueryBuilder basicQuery = buildBoolQueryBuilder(searchRequest);
        nativeSearchQueryBuilder.withQuery(basicQuery);

        // 添加分页,页码从0开始
        nativeSearchQueryBuilder.withPageable(PageRequest.of(searchRequest.getPage() - 1, searchRequest.getSize()));

        // 添加结果集过滤
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));

        // 添加分类和品牌的聚合
        String categoryAggName = "categories";
        String brandAggName = "brands";
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        // 执行查询获取结果集
        AggregatedPage<Goods> goodsPageResult = (AggregatedPage<Goods>) this.goodsRepository.search(nativeSearchQueryBuilder.build());

        // 获取聚合结果集并解析
        List<Map<String, Object>> categories = getCategoryAggResult(goodsPageResult.getAggregation(categoryAggName));
        List<Brand> brands = getBrandAggResult(goodsPageResult.getAggregation(brandAggName));

        // 判断是否是一个分类,只有一个分类时才做规格参数的聚合
        List<Map<String, Object>> specs = null;
        if (!CollectionUtils.isEmpty(categories) && categories.size() == 1) {
            specs = getParamAggResult((Long) categories.get(0).get("id"), basicQuery);
        }
        return new SearchResult(goodsPageResult.getTotalElements(), goodsPageResult.getTotalPages(), goodsPageResult.getContent(), categories, brands, specs);
    }

    /**
     * GET /goods/_search
     * {
     *   "query": {
     *       "bool": {
     *         "must": [
     *           {
     *             "match": {
     *               "all": "手机"
     *             }
     *           }
     *         ],
     *         "filter": {
     *           "term": {
     *             "specs.CPU核数.keyword": "十核"
     *           }
     *         }
     *       }
     *   }
     * }
     * 构建布尔查询
     * @param searchRequest
     * @return BoolQueryBuilder
     */
    private BoolQueryBuilder buildBoolQueryBuilder(SearchRequest searchRequest) {
        // 1.构建布尔查询构建器
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 2.添加基本查询条件
        boolQueryBuilder.must(QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND));
        // 3.添加过滤条件
        // 3.1获取用户选择的过滤信息
        Map<String, Object> filter = searchRequest.getFilter();
        for (Map.Entry<String, Object> entry : filter.entrySet()) {
            String key = entry.getKey();
            if (StringUtils.equals("品牌", key)) {
                key = "brandId";
            } else if (StringUtils.equals("分类", key)) {
                key = "cid3";
            } else {
                key = "specs." + key + ".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key, entry.getValue()));
        }
        return boolQueryBuilder;
    }

    /**
     * 根据查询条件聚合规格参数
     *
     * @param cid
     * @param basicQuery
     * @return
     */
    private List<Map<String, Object>> getParamAggResult(Long cid, QueryBuilder basicQuery) {
        // 自定义查询对象构建
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        // 添加基本查询条件
        nativeSearchQueryBuilder.withQuery(basicQuery);
        // 查询要聚合的规格参数
        List<SpecParam> specParams = this.specificationClient.listSpecParams(null, cid, null, true);
        // 添加规格参数的聚合
        specParams.forEach(specParam -> {
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(specParam.getName())
                    .field("specs."+specParam.getName()+".keyword"));
        });
        // 添加结果集的过滤
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));
        // 执行聚合查询获取聚合结果集
        AggregatedPage<Goods> aggregatedPage = (AggregatedPage<Goods>) this.goodsRepository.search(nativeSearchQueryBuilder.build());
        List<Map<String, Object>> specs = new ArrayList<>();
        // 解析聚合结果集,key:聚合名称(规格参数名称) value:聚合对象
        Map<String, Aggregation> aggregatedMap = aggregatedPage.getAggregations().asMap();
        Set<Map.Entry<String, Aggregation>> entries = aggregatedMap.entrySet();
        for (Map.Entry<String, Aggregation> entry : entries) {
            Map<String, Object> map = new HashMap();
            map.put("k", entry.getKey());
            // 初始化一个List集合收集桶中的key
            List<String> options = new ArrayList<>();
            // 获取聚合
            Aggregation value = entry.getValue();
            StringTerms terms = (StringTerms) value;
            terms.getBuckets().forEach(bucket -> {
                options.add(bucket.getKeyAsString());
            });
            map.put("options", options);
            specs.add(map);
        }
        return specs;
    }

    /**
     * 解析品牌的聚合结果集
     *
     * @param aggregation
     * @return
     */
    private List<Brand> getBrandAggResult(Aggregation aggregation) {
        LongTerms terms = (LongTerms) aggregation;

        /*List<Brand> brands = new ArrayList<>();
        terms.getBuckets().forEach(bucket -> {
            Brand brand = this.brandClient.getBrandById(bucket.getKeyAsNumber().longValue());
            brands.add(brand);
        });
        return brands;*/

        //处理一个集合返回一个新的集合使用stream表达式
        return terms.getBuckets().stream().map(bucket -> {
            return this.brandClient.getBrandById(bucket.getKeyAsNumber().longValue());
        }).collect(Collectors.toList());
    }

    /**
     * 解析分类的聚合结果集
     *
     * @param aggregation
     * @return
     */
    private List<Map<String, Object>> getCategoryAggResult(Aggregation aggregation) {
        LongTerms terms = (LongTerms) aggregation;

        // 处理一个List返回一个新的List其中的元素如果是map创建map遍历原始List处理元素放入map中返回一个装着map的List
        return terms.getBuckets().stream().map(bucket -> {
            Map<String, Object> map = new HashMap<>();
            Long id = bucket.getKeyAsNumber().longValue();
            List<String> names = this.categoryClient.listNamesByIds(Arrays.asList(id));
            map.put("id", id);
            map.put("name", names.get(0));
            return map;
        }).collect(Collectors.toList());
    }

    public void save(Long id) throws IOException {
        Spu spu = this.goodsClient.getSpuById(id);
        Goods goods = this.buildGoods(spu);
        this.goodsRepository.save(goods);
    }

    public void delete(Long id) {
        this.goodsRepository.deleteById(id);
    }
}
