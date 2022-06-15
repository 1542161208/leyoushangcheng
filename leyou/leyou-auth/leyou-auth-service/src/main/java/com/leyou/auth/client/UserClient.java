package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Create by lx on 2021/11/27.调用用户微服务下Feign接口
 */
@FeignClient(value  ="user-service")
public interface UserClient extends UserApi {
}
