package com.leyou.item.pojo;

import javax.persistence.*;

/**
 * @description 每一个规格参数组对应规格参数实体类
 * @author lx
 * @date 2020/12/13
 */
@Table(name = "tb_spec_param")
public class SpecParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     * 规格参数主键
     */
    private Long id;
    /**
     * 分类id
     */
    private Long cid;
    /**
     * 该规格参数属于哪个组
     */
    private Long groupId;
    /**
     * 参数名称
     */
    private String name;
    /**
     * 该规格参数是否是数字
     * 如果属性名与字段名不一致使用Column注解这个numeric在mysql中是关键字所以映射一下``这个是标记一下它不是一个关键字
     * 就是一个列名符号去掉会报错因为是关键字
     */
    @Column(name = "`numeric`")
    private Boolean numeric;
    /**
     * 如果是数字单位是什么
     */
    private String unit;
    /**
     * 该规格参数是否是sku通用属性
     */
    private Boolean generic;
    /**
     * 是否用于搜索
     */
    private Boolean searching;
    /**
     * 如果该规格类型参数是搜索用的并且搜索条件是按照范围来的
     */
    private String segments;

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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNumeric() {
        return numeric;
    }

    public void setNumeric(Boolean numeric) {
        this.numeric = numeric;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getGeneric() {
        return generic;
    }

    public void setGeneric(Boolean generic) {
        this.generic = generic;
    }

    public Boolean getSearching() {
        return searching;
    }

    public void setSearching(Boolean searching) {
        this.searching = searching;
    }

    public String getSegments() {
        return segments;
    }

    public void setSegments(String segments) {
        this.segments = segments;
    }

}
