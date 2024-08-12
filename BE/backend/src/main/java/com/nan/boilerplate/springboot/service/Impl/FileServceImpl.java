package com.nan.boilerplate.springboot.service.Impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.nan.boilerplate.springboot.service.FileService;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServceImpl implements FileService {
    @Value("${API-KEY.UserAPI}")
    String Userapi;
    @Value("${API-KEY.UserKey}")
    String UsersecretKey;
    @Value("${API-KEY.CompanyAPI}")
    String Companyapi;
    @Value("${API-KEY.CompanyKey}")
    String CompanyKey;
    private String uploadDir = "src/main/resources/static/uploads";

    public void FileUpload(MultipartFile multipartFile, String uuid) {
        //uuid로 저장해야함
        // File.seperator 는 OS종속적이다.
        // Spring에서 제공하는 StringUtils.cleanPath()를 통해서 ../ 내부 점들에 대해서 사용을 억제한다
        Path copyOfLocation = Paths.get(uploadDir);
        try {
            Files.copy(multipartFile.getInputStream(), copyOfLocation.resolve(uuid), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> NaverOCRCompany(MultipartFile multipartFile, String uuid) {
        List<String> parseData = null;

        try {
            // JSON 데이터 구성
            JSONObject json = new JSONObject();
            json.put("version", "V2");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", System.currentTimeMillis());

            JSONObject image = new JSONObject();
            image.put("format", multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")+1));
            image.put("data", Base64.encodeBase64String(multipartFile.getBytes()));
            image.put("name", "StarLightCompany");

            JSONArray images = new JSONArray();
            images.put(image);
            json.put("images", images);

            // RestTemplate 인스턴스 생성
            RestTemplate restTemplate = new RestTemplate();
            // HttpHeaders 설정
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.set("X-OCR-SECRET", CompanyKey);

            // HttpEntity 생성 (요청 바디와 헤더 포함)
            HttpEntity<String> requestEntity = new HttpEntity<>(json.toString(), httpHeaders);

            // POST 요청 보내기
            ResponseEntity<String> response = restTemplate.postForEntity(Companyapi, requestEntity, String.class);

            // 응답 처리
            String responseBody = response.getBody();
            System.out.println(responseBody);
            parseData = jsonparse(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseData;
    }

    private static List<String> jsonparse(String response) throws Exception {
        // json 파싱 (기존 코드 사용)
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> parseData = new ArrayList<>();
        try {
            // JSON 문자열을 JsonNode 객체로 변환
            JsonNode jsonNode = objectMapper.readTree(response);

            parseData.add(jsonNode.path("images")
                    .path(0)
                    .path("bizLicense")
                    .path("result")
                    .path("birth")
                    .path(0)
                    .path("text").asText());
            parseData.add(jsonNode.path("images")
                    .path(0)
                    .path("bizLicense")
                    .path("result")
                    .path("bisAddress")
                    .path(0)
                    .path("text").asText());
            parseData.add(jsonNode.path("images")
                    .path(0)
                    .path("bizLicense")
                    .path("result")
                    .path("registerNumber")
                    .path(0)
                    .path("text").asText());
            parseData.add(jsonNode.path("images")
                    .path(0)
                    .path("bizLicense")
                    .path("result")
                    .path("registerNumber")
                    .path(0)
                    .path("text").asText().replace("-",""));
            parseData.add(jsonNode.path("images")
                    .path(0)
                    .path("bizLicense")
                    .path("result")
                    .path("bisType")
                    .path(0)
                    .path("text").asText());
            parseData.add(jsonNode.path("images")
                    .path(0)
                    .path("bizLicense")
                    .path("result")
                    .path("companyName")
                    .path(0)
                    .path("text").asText());
            
            System.out.println(parseData);
            return parseData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseData;//빈 리스트
    }
}

