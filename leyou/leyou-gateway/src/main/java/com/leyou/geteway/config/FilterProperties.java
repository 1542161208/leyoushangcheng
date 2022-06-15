package com.leyou.geteway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Create by lx on 2021/12/04.封装配置文件中Filter信息
 */
@ConfigurationProperties(prefix = "leyou.filter")
public class FilterProperties {
    private static final Logger logger = LoggerFactory.getLogger(FilterProperties.class);
    private List<String> allowPaths;

    public List<String> getAllowPaths() {
        return allowPaths;
    }

    public void setAllowPaths(List<String> allowPaths) {
        this.allowPaths = allowPaths;
    }
}
