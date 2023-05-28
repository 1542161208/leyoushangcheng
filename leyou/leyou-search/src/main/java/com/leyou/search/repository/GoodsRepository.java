package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @description
 * @author xiang
 * @date 2021/05/02
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
