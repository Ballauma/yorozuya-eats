package com.yorozuya.controller.admin;

import com.yorozuya.constant.MessageConstant;
import com.yorozuya.result.Result;
import com.yorozuya.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Ballauma
 */
@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;


    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        log.info("upload file: {}", file);

        try {
            String originalFilename = file.getOriginalFilename();
            // 截取文件扩展名
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filePath = aliOssUtil.upload(file.getBytes(), UUID.randomUUID().toString() + extension);
            return Result.success(filePath);
        } catch (IOException e) {
            log.info("上传文件失败: {}", e.getMessage());
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
