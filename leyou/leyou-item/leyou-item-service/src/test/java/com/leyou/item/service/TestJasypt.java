package com.leyou.item.service;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xiang
 * @description
 * @date 2021/07/24
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestJasypt{
    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void testJasypt (){
        String result;
        result = stringEncryptor.encrypt("leyou");
//        result = stringEncryptor.decrypt("3ntwGeIxr28eAmZAMniEBdkecJX4yx7y");
        System.out.println(result);
    }
}