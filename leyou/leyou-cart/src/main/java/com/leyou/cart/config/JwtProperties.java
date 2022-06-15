package com.leyou.cart.config;

import com.leyou.common.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * Create by lx on 2021/12/04.封装配置文件中JWT信息
 */
@ConfigurationProperties(prefix = "leyou.jwt")
public class JwtProperties {
    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);
    /**
     * 公钥路径
     */
    private String pubKeyPath;
    /**
     * cookie名字
     */
    private String cookieName;
    /**
     * 公钥对象
     */
    private PublicKey publicKey;

    /**
     * @PostContruct:在构造方法执行之后执行该方法
     */
    @PostConstruct
    public void init(){
        try {
            // 获取公钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            logger.error("初始化公钥和私钥失败！", e);
            throw new RuntimeException();
        }
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
}
