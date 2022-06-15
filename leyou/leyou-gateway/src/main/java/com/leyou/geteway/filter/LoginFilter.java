package com.leyou.geteway.filter;

import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import com.leyou.geteway.config.FilterProperties;
import com.leyou.geteway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by lx on 2021/12/04.登录过滤器
 */
@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private FilterProperties filterProperties;

    // 路由之前判断先判断是否登录进行前置过滤
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    // 返回false将不会执行run方法
    @Override
    public boolean shouldFilter() {
        // 获取白名单路径
        List<String> allowPaths = this.filterProperties.getAllowPaths();
        // 初始化Zuul网关运行上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        // 获取request对象
        HttpServletRequest request = currentContext.getRequest();
        // 获取当前请求路径
        String requestURL = request.getRequestURL().toString();
        for (String allowPath : allowPaths) {
            if (StringUtils.contains(requestURL, allowPath)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 初始化Zuul网关运行上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        // 获取request对象
        HttpServletRequest request = currentContext.getRequest();
        // 获取Cookie中的token信息
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());

/*        if (StringUtils.isBlank(token)) {
            // 设置不进行转发
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }*/

        // 不为空开始解析
        try {
            JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            // 设置不进行转发
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }

        return null;
    }
}
