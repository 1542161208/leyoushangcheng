package com.leyou.search.pojo;

import java.util.Map;

/**
 * @author lx
 * @description 查询请求对象
 * @date 2021/05/04
 */
public class SearchRequest {
    /**
     * 搜索条件
     */
    private String key;
    /**
     * 当前页
     */
    private Integer page;
    /**
     * 点击的规格参数放入集合的过滤对象
     */
    private Map<String, Object> filter;
    /**
     * 每页大小，不从页面接收，而是固定大小
     */
    private static final Integer DEFAULT_SIZE = 20;
    /**
     * 默认页
     */
    private static final Integer DEFAULT_PAGE = 1;

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if (page == null) {
            return DEFAULT_PAGE;
        }
        // 获取页码时做一些校验，不能小于1
        return Math.max(DEFAULT_PAGE, page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return DEFAULT_SIZE;
    }
}
