package com.project.milenix.file.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class UserFileStorageProperties {

    @Autowired
    private ResourceLoader resourceLoader;
    @Value("${file.upload-dir-user}")
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir.replace("static", "");
    }

    public String getAbsoluteUploadDir() {
        System.out.println(uploadDir);
        return getClass().getClassLoader().getResource(uploadDir).getPath();

    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}