package com.leyou.cart.pojo;

/**
 * 登录状态购物车对象
 */
public class Cart {
    // 用户id
    private Long userId;

    // 商品id
    private Long skuId;

    // 标题
    private String title;

    // 图片
    private String image;

    // 加入购物车时的价格
    private Long price;

    // 购买数量
    private Integer num;

    // 商品规格参数
    private String ownSpec;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getOwnSpec() {
        return ownSpec;
    }

    public void setOwnSpec(String ownSpec) {
        this.ownSpec = ownSpec;
    }
}