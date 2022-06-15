package com.leyou.common.test;

import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.common.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author lx
 * &#064;description
 * &#064;date 2021/11/27
 */
public class JwtTest{
    // 生成公钥、私钥的目录
    private static final String pubKeyPath = "C:\\tmp\\rsa\\rsa.pub";
    private static final String priKeyPath = "C:\\tmp\\rsa\\rsa.pri";

    private PublicKey publicKey;
    private PrivateKey privateKey;

    // 测试根据盐值生成公钥、私钥
    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    // 测试根据私钥生成token
    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTYzNzk4NzU4NX0.QYfG_NiVOr8tLoR_VvfsLSVfLWxiz-4KcRGn26ksN61OPX_0XpPG5LzsmA_buJhnvpHcQMkr9UIKUD1DcrlA2ZkAPD0kaqt7lKa1b-5E34OcW98slXL3CVbFDiSjKi8mGsLonnYiyq5yN_W7qPXT8ZmpF0wPHuPKtIw93ZSrd4c";
        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}