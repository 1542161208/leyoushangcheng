package com.leyou.auth.config;

import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xiang
 * @description SS
 * @date 2021/11/28
 */
@Component
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        //消息转换器，Spring Boot环境可省略，只需要添加相关依赖即可
        ///List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        ///messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ///messageConverters.add(new FormHttpMessageConverter());
        ///messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
        ///messageConverters.add(new MappingJackson2HttpMessageConverter());

        RestTemplate restTemplate = new RestTemplate(factory);
        ///restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory(){
        // HttpClient连接池，长连接保持30秒
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);

        // 设置总连接数
        connectionManager.setMaxTotal(1000);
        // 设置同路由的并发数
        connectionManager.setDefaultMaxPerRoute(1000);

        //设置header
        List<Header> headers = new ArrayList<>();
//        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.04"));
        headers.add(new BasicHeader("Connection", "keep-alive"));

        // 创建HttpClient
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultHeaders(headers)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .build();

        // 创建HttpComponentsClientHttpRequestFactory实例
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        // 设置客户端和服务端建立连接的超时时间
        requestFactory.setConnectTimeout(5000);
        // 设置客户端从服务端读取数据的超时时间
        requestFactory.setReadTimeout(5000);
        // 设置从连接池获取连接的超时时间，不宜过长
        requestFactory.setConnectionRequestTimeout(200);
        // 缓冲请求数据，默认为true。通过POST或PUT大量发送数据时，建议将此更改为false，以免耗尽内存
        requestFactory.setBufferRequestBody(false);

        return requestFactory;
    }
}
