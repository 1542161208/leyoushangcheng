package com.leyou.item.pojo;

import javax.persistence.*;
import java.util.List;

/**
 * @description 规格参数对象实体类
 * @author lx
 * @date 2020/12/13
 */
// 使用的是SpringDataJPA的映射方式
@Table(name = "tb_spec_group")
public class SpecGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     * 规格参数组主键
     */
    private Long id;
    /**
     * 商品分类id一个分类下有多个规格参数组
     */
    private Long cid;
    /**
     * 规格参数组名称
     */
    private String name;
    /**
     * 每一个规格参数组下维护多个规格参数
     * 因为表中没有这个字段使用Transient表示忽略该字段
     */
    @Transient
    private List<SpecParam> params;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SpecParam> getParams() {
        return params;
    }

    public void setParams(List<SpecParam> params) {
        this.params = params;
    }
}
