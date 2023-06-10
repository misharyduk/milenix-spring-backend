package com.project.milenix.file.service;

import com.project.milenix.file.exception.FileStorageException;
import com.project.milenix.file.util.ArticleFileStorageProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ArticleFileStorageService {
    private final ArticleFileStorageProperties fileStorageProperties;
    private Path fileStorageLocation;

    public ArticleFileStorageService(ArticleFileStorageProperties fileStorageProperties){
        this.fileStorageProperties = fileStorageProperties;

        this.fileStorageLocation = Paths.get(fileStorageProperties.getAbsoluteUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex){
            throw new FileStorageException("Could not create the directory where the upload files will be stored", ex);
        }
    }

    private void createDirectory(Integer id){
        this.fileStorageLocation = Paths.get(
                        fileStorageProperties.getAbsoluteUploadDir() + "/" + id)
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex){
            throw new FileStorageException("Could not create the directory where the upload files will be stored", ex);
        }
    }

    public String storeFile(Integer id, MultipartFile file){
        createDirectory(id);

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try{
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex){
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(Integer id, String fileName){
        try {
            Path filePath = this.fileStorageLocation.resolve(id + "/" + fileName).normalize();
            System.out.println(fileStorageLocation.toAbsolutePath());
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            } else {
                throw new FileStorageException("File not found " + fileName);
            }
        } catch (MalformedURLException ex){
            throw new FileStorageException("File not found " + fileName);
        }
    }
}