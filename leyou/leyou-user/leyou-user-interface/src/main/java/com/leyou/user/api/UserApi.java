package com.leyou.user.api;

import com.leyou.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Ctreate by lx on 2021/11/27.用户信息接口
 */

public interface UserApi {
    @GetMapping("/query")
     User queryUser(@RequestParam("username")String username, @RequestParam("password")String password);
}
