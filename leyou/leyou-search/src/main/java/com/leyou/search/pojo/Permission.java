package com.leyou.search.pojo;

import java.util.List;

/**
 * @author lx
 * @description 权限类
 * @date 2021/07/15
 */
public class Permission {
    private static final long serialVersionUID = 701576040977174343L;
    // 主键
    private Integer id;
    // 权限ID
    private String permissionid;
    // 权限名称
    private String permissionName;
    // 父级ID
    private String permissionParentid;
    // 权限类型
    private Integer dictPermissionType;
    // 排序号
    private Integer sortno;
    List<Permission> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermissionid() {
        return permissionid;
    }

    public void setPermissionid(String permissionid) {
        this.permissionid = permissionid;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionParentid() {
        return permissionParentid;
    }

    public void setPermissionParentid(String permissionParentid) {
        this.permissionParentid = permissionParentid;
    }

    public Integer getDictPermissionType() {
        return dictPermissionType;
    }

    public void setDictPermissionType(Integer dictPermissionType) {
        this.dictPermissionType = dictPermissionType;
    }

    public Integer getSortno() {
        return sortno;
    }

    public void setSortno(Integer sortno) {
        this.sortno = sortno;
    }

    public List<Permission> getChildren() {
        return children;
    }

    public void setChildren(List<Permission> children) {
        this.children = children;
    }
}
