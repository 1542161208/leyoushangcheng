package com.leyou.search.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @description 搜索微服务中调用商品微服务的Feign接口
 * @author lx
 * @date 2021/05/02
 */
@FeignClient(value = "item-service")
public interface CategoryClient extends CategoryApi {
}
