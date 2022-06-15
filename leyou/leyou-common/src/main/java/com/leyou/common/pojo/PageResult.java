package com.leyou.common.pojo;

import java.util.List;

/**
 * Created by lx on 2020/10/20.分页对象
 */
public class PageResult<T> {
    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Integer totalPage;

    /**
     * 这一页的数据
     */
    private List<T> items;

    public PageResult() {
    }

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total, Integer totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }

    public Long getTotal() {
        return total;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public List<T> getItems() {
        return items;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", totalPage=" + totalPage +
                ", items=" + items +
                '}';
    }
}
