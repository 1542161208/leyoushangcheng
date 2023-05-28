package com.leyou.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author xiang
 * @description 用户实体类
 * @date 2021/10/05
 */
@Table(name = "tb_user")
public class User {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 用户名
     */
    @Length(min = 4, max = 30, message = "用户名的长度在4-30位!")
    private String username;
    /**
     * 密码
     * @JsonIgnore:对象序列化为json字符串时忽略该属性
     */
    @Length(min = 4, max = 30, message = "密码的长度在4-30位!")
    @JsonIgnore
    private String password;
    /**
     * 电话
     */
    @Pattern(regexp = "^1[35678]\\d{9}$", message = "手机号格式不正确!")
    private String phone;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 密码的盐值
     */
    @JsonIgnore
    private String salt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", created=" + created +
                ", salt='" + salt + '\'' +
                '}';
    }
}
