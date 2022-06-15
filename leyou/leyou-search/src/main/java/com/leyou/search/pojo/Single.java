package com.leyou.search.pojo;

/**
 * @author lx
 * @description de
 * @date 2021/07/15
 */
public class Single {
    // 1.私有化构造函数
    private Single() {
    }

    // 2.初始化一个私有、静态的对象引用(final无法new对象)
    private static Single s = null;

    // 3.提供一个公共的静态的获取对象的方法
    public static synchronized Single getInstance() {
        if (s == null) {
            s = new Single();
        }
        return s;
    }
}
