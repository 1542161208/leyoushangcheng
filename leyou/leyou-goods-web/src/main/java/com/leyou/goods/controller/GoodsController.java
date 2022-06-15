package com.leyou.goods.controller;

import com.leyou.goods.service.GoodsHtmlService;
import com.leyou.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * Created by lx on 2021/10/03.商品详情跳转接口
 */
@Controller
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable Long id, Model model) {
        Map<String, Object> map = goodsService.loadData(id);
        model.addAllAttributes(map);
        this.goodsHtmlService.createHtml(id);
        return "item";
    }
}
