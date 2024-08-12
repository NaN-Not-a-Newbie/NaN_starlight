package com.nan.boilerplate.springboot.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    void FileUpload(MultipartFile file, String uuid);

    List<String> NaverOCRCompany(MultipartFile multipartFile, String uuid);
}
