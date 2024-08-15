package com.nan.boilerplate.springboot.service.Impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.security.dto.CompanyRegistrationRequest;
import com.nan.boilerplate.springboot.security.dto.UserApplyRequest;
import com.nan.boilerplate.springboot.security.dto.UserRegistrationRequest;
import com.nan.boilerplate.springboot.security.jwt.JwtTokenManager;
import com.nan.boilerplate.springboot.security.jwt.JwtTokenService;
import com.nan.boilerplate.springboot.security.mapper.UserMapperImpl;
import com.nan.boilerplate.springboot.security.service.UserDetailsServiceImpl;
import com.nan.boilerplate.springboot.security.service.UserService;
import com.nan.boilerplate.springboot.security.utils.SecurityConstants;
import com.nan.boilerplate.springboot.service.FileService;
import com.nan.boilerplate.springboot.service.JobOfferService;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
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
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FileServceImpl implements FileService {
    private final UserService userService;
    private final JobOfferService jobOfferService;
    private final JwtTokenManager jwtTokenManager;
    @Value("${API-KEY.UserAPI}")
    String Userapi;
    @Value("${API-KEY.UserKey}")
    String UsersecretKey;
    @Value("${API-KEY.CompanyAPI}")
    String Companyapi;
    @Value("${API-KEY.CompanyKey}")
    String CompanyKey;
    String dst="BE/backend/src/main/resources/static/contracts/";

    public void FileDownloadContract(HttpServletResponse response) {
        String myName = SecurityConstants.getAuthenticatedUsername();

        User user = userService.findByUsername(myName).get();
        String paper="jobPaper.pdf";
        try {
            //uuid로 검색
            String filePath = dst+user.getPaperPath();

            //저장된 디렉토리 위치+파일 이름
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename="+paper);

            //내가 보낼 파일의 이름 filename으로 설정해서 전송
            java.io.File file = new java.io.File(filePath);
            response.setContentLengthLong(file.length());
            //파일 객체 생성
            FileInputStream fis=new FileInputStream(file);
            //파일 읽어서 저장
            OutputStream out=response.getOutputStream();
            //보낼거
            byte[] bytes=new byte[4096];
            //읽을 크기
            int read=0;
            while((read=fis.read(bytes))!=-1){
                out.write(bytes,0,read);
                //쓰기
            }
            //없을 때까지 읽기
            out.flush();
            //출력 후 비우기
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        }

    @Override
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

            return parseData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseData;
    }

    @Override
    public void makeContract(UserApplyRequest userApplyRequest) {
        //표준 근로계약서 pdf로 받고 사진 글자 삽입
        String uuid= UUID.randomUUID().toString();
        String src="BE/backend/src/main/resources/static/contracts/sample/표준근로계약서+최종.hwp.pdf";
        String dst="BE/backend/src/main/resources/static/contracts/"+uuid+".pdf";
        String sign="BE/backend/src/main/resources/static/sign";
        String myName = SecurityConstants.getAuthenticatedUsername();
        User user = userService.findByUsername(myName).get();

        JobOffer jobOffer = jobOfferService.getJobOfferById(userApplyRequest.getJobOfferId()).get();

        try{
            File dstFile = new File(dst);
            dstFile.getParentFile().mkdirs();  // 디렉토리가 없으면 생성
            PdfFont font = PdfFontFactory.createFont("c:/windows/fonts/H2MJRE.TTF", PdfEncodings.IDENTITY_H);

            PdfReader reader = new PdfReader(src);
            PdfWriter writer = new PdfWriter(dst);
            PdfDocument pdf = new PdfDocument(reader, writer);

            Document document = new Document(pdf);

            String Name = user.getName();
            String companyName = jobOffer.getCompany().getCompanyName();
            String location = jobOffer.getLocation();
            System.out.println(location);
            String salary = jobOffer.getSalary().toString();
            String salaryType = jobOffer.getSalaryType().toString();
            
            Paragraph paragraphName = new Paragraph(Name).setFont(font);
            Paragraph paragraphcompanyName = new Paragraph(companyName).setFont(font);
            Paragraph paragraphLocation = new Paragraph(location).setFont(font);

            Paragraph paragraphSalary = new Paragraph(salary).setFont(font);
            Paragraph paragraphSalaryType = new Paragraph(salaryType).setFont(font);

            paragraphName.setFixedPosition(1, 120, 740, 700); // 페이지 1, x=50, y=1050, 너비=700 포인트
            paragraphcompanyName.setFixedPosition(1, 300, 740, 600);
            paragraphLocation.setFixedPosition(1, 200, 140, 1500);
            paragraphSalary.setFixedPosition(1, 210, 560, 400);
            paragraphSalaryType.setFixedPosition(1, 120, 560, 300);

            document.add(paragraphName);
            document.add(paragraphcompanyName);
            document.add(paragraphLocation);
            document.add(paragraphSalary);
            document.add(paragraphSalaryType);

            String imagePath = sign+"/"+user.getSignPath();
            System.out.println(imagePath);// 삽입할 이미지 경로
            ImageData imageData = ImageDataFactory.create(imagePath);
            com.itextpdf.layout.element.Image image = new Image(imageData);

            // 이미지 크기 설정 (예: 이미지의 너비를 200포인트, 높이를 100포인트로 설정)
            image.scaleToFit(200, 100);

            // 이미지의 위치 설정 (예: x=300, y=500 위치에 삽입)
            image.setFixedPosition(1, 300, 0);

            // 이미지를 문서에 추가
            document.add(image);

            document.close();
            String userName = SecurityConstants.getAuthenticatedUsername();
            userService.paperPathAdd(userName,uuid+".pdf");
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void backgroundCutout(InputStream inputStream, String token) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String dst = "BE/backend/src/main/resources/static/sign/" + uuid + ".png";

        try {
            // 이미지를 읽어오기 (inputImageStream은 PNG 이미지 InputStream)
            BufferedImage image = ImageIO.read(inputStream);

            BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

            // 각 픽셀을 확인하며 흰색 배경을 투명하게 처리
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int rgb = image.getRGB(x, y);
                    Color color = new Color(rgb, true);
                    if (color.getRed() == 255 && color.getGreen() == 255 && color.getBlue() == 255) {
                        // 흰색 픽셀을 투명하게 설정
                        newImage.setRGB(x, y, 0x00FFFFFF & rgb);
                    } else {
                        // 원래 색상 유지
                        newImage.setRGB(x, y, rgb);
                    }
                }
            }
            System.out.println(token);

            byte[] decodedBytes = java.util.Base64.getDecoder().decode(token);
            String decodedStr = new String(decodedBytes);
            System.out.println(decodedStr);
            String jwtToUsername = jwtTokenManager.getUsernameFromToken(decodedStr);
            System.out.println(jwtToUsername);
            userService.signPathAdd(jwtToUsername,uuid +".png");
            // 수정된 이미지를 PNG 파일로 저장
            File outputImageFile = new File(dst);
            ImageIO.write(newImage, "PNG", outputImageFile);
            // 저장된 파일 경로를 사용자 서비스에 추가




        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    .path("bisAddress")
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






