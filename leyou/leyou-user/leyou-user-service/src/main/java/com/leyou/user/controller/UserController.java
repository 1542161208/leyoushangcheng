package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author xiang
 * @description 用户控制层
 * @date 2021/10/05
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 校验数据是否可用
     *
     * @param data
     * @param type
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data, @PathVariable(value = "type") Integer type) {
        Boolean boo = this.userService.checkData(data, type);
        if (boo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(boo);
    }

    /**
     * 根据用户手机号生成验证码并发送给用户发送短信
     *
     * @param phone
     * @return
     */
    @PostMapping("code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone") String phone) {
        this.userService.sendVerifyCode(phone);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 用户注册
     *
     * @param user
     * @param code
     * @return ResponseEntity<Void>
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code) {
        this.userService.register(user, code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 查询用户
     * @param ///username
     * @param ///password
     * @return ResponseEntity<User>
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username")String username, @RequestParam("password")String password) {
        System.out.println("进入方法：username："+username+",,password:"+password);

        User user = this.userService.getUser(username, password);
         if (user == null) {
            return ResponseEntity.badRequest().build();
         }

      return ResponseEntity.ok(user);
    }
}
