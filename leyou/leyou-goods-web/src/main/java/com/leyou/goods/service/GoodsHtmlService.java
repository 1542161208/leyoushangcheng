package com.leyou.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * @author xiang
 * @description 生成商品详情模板缓存
 * @date 2021/10/03
 */
@Service
public class GoodsHtmlService {
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private GoodsService goodsService;

    public void createHtml(Long spuId) {
        // 初始化运行上下文
        Context context = new Context();
        // 设置数据模型
        context.setVariables(this.goodsService.loadData(spuId));
        PrintWriter printWriter = null;
        try {
            // 把静态文件生成到服务器本地
            File file = new File("C:\\Users\\LiXiang\\Downloads\\nginx\\nginx-1.14.0\\html\\item\\" + spuId + ".html");
            printWriter = new PrintWriter(file);
            this.templateEngine.process("item", context, printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    public void deleteHtml(Long id) {
        File file = new File("C:\\Users\\LiXiang\\Downloads\\nginx\\nginx-1.14.0\\html\\item\\" + id + ".html");
        file.deleteOnExit();
    }
}
