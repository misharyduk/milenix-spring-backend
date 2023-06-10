package com.project.milenix.file.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ArticleFileStorageProperties {
    @Value("${file.upload-dir-article}")
    private String uploadDir;

    public String getAbsoluteUploadDir() {
        System.out.println(uploadDir);
        return getClass().getClassLoader().getResource(uploadDir).getPath();

    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
