package com.leyou.goods.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Created by lx on 2021/05/02.搜索微服务中调用Feign接口
 */
@FeignClient(value = "item-service")
public interface SpecificationClient extends SpecificationApi {
}
