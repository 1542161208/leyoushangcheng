package com.leyou.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Create by lx on 2021/11/27.授权中业务层
 */
@Service
public class AuthService {
    @Autowired
    private UserClient userClient;
    
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RestTemplate restTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String accredit(String username, String password){
        //1.根据用户名和密码查询
        User user = this.userClient.queryUser(username, password);

        //1.GET请求参数
        ///Map<String,Object> paramMap = new HashMap<>();
        ///paramMap.put("username",username);
        ///paramMap.put("password",password);

        //2.转化请求路径
        ///String url = "http://localhost:8085/query?username=" + username + "&password=" + password;
        ///UriComponents uriComponents = UriComponentsBuilder.fromUriString(url).build().expand(paramMap).encode();

        //3.发送get请求，接收json字符串
        ///String result = this.restTemplate.getForObject(url, String.class);

        //4.转化为实体类
        ///User user = MAPPER.convertValue(result, User.class);

        //2.判断用户
        if(user==null){
            return null;
        }

        //3.jwtUtils生成jwt类型token
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            return JwtUtils.generateToken(userInfo, this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
