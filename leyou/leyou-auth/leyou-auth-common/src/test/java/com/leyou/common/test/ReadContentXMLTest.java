package com.leyou.common.test;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Test;

import java.io.*;
import java.util.List;

/**
 * Created by lx on 2021/11/29. 内容
 */
public class ReadContentXMLTest {
    @Test
    public void test(){
        //1.创建SAXBuilder对象
        SAXBuilder saxBuilder = new SAXBuilder();
        //2.创建输入流
        InputStream is = null;
        try {
            is = new FileInputStream(new File("C:\\Users\\LiXiang\\dom.xml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //3.将输入流加载到build中
        Document document = null;
        try {
            document = saxBuilder.build(is);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //4.获取根节点
        Element rootElement = document.getRootElement();
        //5.获取子节点
        List<Element> children = rootElement.getChildren();
        for (int i = 1; i <= children.size(); i++) {
            if(i==100){
                System.out.println("======遍历第" + i + "条======");
            }

            List<Element> childrenList = children.get(i).getChildren();
            System.out.println("======获取子节点-start======");
            for(int j = 0; j < childrenList.size(); j++){
                Element element = childrenList.get(j);
                String value = element.getValue();
                if(0==j){
                    System.out.println("value:" + value);
                } else if(1==j){
                    System.out.println("value:" + value);
                } else if(2==j){
                    System.out.println("value:" + value);
                } else if(3==j){
                    System.out.println("value:" + value);
                } else if(4==j){
                    System.out.println("value:" + value);
                } else if(5==j){
                    System.out.println("value:" + value);
                } else if(6==j){
                    System.out.println("value:" + value);
                } else if(7==j){
                    System.out.println("value:" + value);
                } else if(8==j){
                    System.out.println("value:" + value);
                }
            }
            System.out.println("======获取子节点-end======");
        }
    }
}
