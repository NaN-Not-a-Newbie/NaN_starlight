package com.nan.boilerplate.springboot.service;

import com.nan.boilerplate.springboot.security.dto.UserApplyRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public interface FileService {

    void FileDownloadContract(HttpServletResponse response);

    List<String> NaverOCRCompany(MultipartFile multipartFile, String uuid);

    void makeContract(UserApplyRequest userApplyRequest);

    void backgroundCutout(InputStream inputPdfStream, String username) throws IOException ;

    boolean fileCheck(MultipartFile multipartFile);
}
