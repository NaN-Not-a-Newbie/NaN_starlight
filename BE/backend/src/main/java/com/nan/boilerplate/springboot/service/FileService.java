package com.nan.boilerplate.springboot.service;

import com.nan.boilerplate.springboot.security.dto.UserApplyRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public interface FileService {
    void FileUploadUserSign(MultipartFile file, String uuid);

    void FileDownloadContract();

    List<String> NaverOCRCompany(MultipartFile multipartFile, String uuid);

    void makeContract(UserApplyRequest userApplyRequest);

    void backgroundCutout(InputStream inputPdfStream) throws IOException ;
}
