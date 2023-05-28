package com.leyou.elasticsearch.test;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description 导入Elasticsearch索引库的测试类
 * @author xiang
 * @date 2021/05/02
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private SearchService searchService;

    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void test(){
        // ElasticSearchTemplate模板创建索引库
        this.elasticsearchTemplate.createIndex(Goods.class);
        // 创建映射
        this.elasticsearchTemplate.putMapping(Goods.class);

        Integer page = 1;
        Integer rows = 100;

        // 分批次查询一次100条商品数据循环导入索引库中
        do {
            // 获取分页对象
            PageResult<SpuBo> pageResult = this.goodsClient.searchSpuByPage(null, null, page, rows);
            // 获取分页对象中的商品集合
            List<SpuBo> items = pageResult.getItems();
            // 处理一个集合返回一个集合使用Stream表达式:List<SpuBo>==>List<Goods>
            List<Goods> listGoods = items.stream().map(spuBo -> {
                try {
                    return this.searchService.buildGoods(spuBo);
                } catch (IOException e) {
                    // System.out.println("111");
                    e.printStackTrace();
                }

                return null;
            }).collect(Collectors.toList());
            // System.out.println("222");
            // 执行新增数据的方法
            this.goodsRepository.saveAll(listGoods);
            // System.out.println("333");
            // 导入成功后记录数刷新
            rows = items.size();
            //System.out.println("rows:"+rows);
            // 导入成功之后页数加一
            page++;
            //System.out.println("page:"+page);
        } while (rows == 100);
    }
}
