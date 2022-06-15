package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author lx
 * @create 2020/11/09
 */
@Service
public class UploadService {
    // 打印日志类
    private static final Logger logger = LoggerFactory.getLogger(UploadService.class);

    // 文件类型所属类型集合
    private static final List<String> CONTENT_TYPES = Arrays.asList("image/gif", "image/jpeg");

    @Autowired
    FastFileStorageClient storageClient;

    /**
     * 页面图片上传
     *
     * @param file
     * @return ResponseEntity<String>
     */
    public String uploadImage(MultipartFile file) throws IOException {
        // 1)校验文件类型
        // 获取上传的图片源文件
        String originalFilename = file.getOriginalFilename();
        // 获取文件类型
        String contentType = file.getContentType();
        // 如果不在所属类型集合内
        if (!CONTENT_TYPES.contains(contentType)) {
            // 可以使用多个占位符的写法
            // logger.info("{}文件类型不合法:{}",param1,param2);
            // 文件类型不合法，直接返回null
            logger.info("文件类型不合法:{}", originalFilename);
            return null;
        }

        // 2)校验文件内容(判断是否是一个图片)
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                logger.info("获取文件流信息出错:{}", originalFilename);
                return null;
            }
        } catch (IOException e) {
            logger.info("获取文件流信息出错:{}", originalFilename);
            e.printStackTrace();
        }

        // 3)保存到服务器
/*        try {
            file.transferTo(new File("C:\\Users\\LiXiang\\Pictures\\leyou\\" + originalFilename));
        } catch (IOException e) {
            logger.info("文件保存到服务器出错:{}", originalFilename);
            e.printStackTrace();
        }
        return "http://image.leyou.com/" + originalFilename;*/

        // 3)将图片上传到FastDFS
        // 获取文件后缀名
        String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        // 上传
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);

        // 4)返回url,进行回显
        // update by lx 20201115
        return "http://image.leyou.com/" + storePath.getFullPath();
    }
}
