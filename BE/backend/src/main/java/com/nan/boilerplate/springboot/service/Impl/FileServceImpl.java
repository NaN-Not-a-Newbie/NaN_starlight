package com.nan.boilerplate.springboot.service.Impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import com.nan.boilerplate.springboot.exceptions.BadRequestException;
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
import lombok.RequiredArgsConstructor;

import org.apache.tomcat.util.codec.binary.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.*;
import java.util.List;


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
        Optional<User> userOptional = userService.findByUsername(myName);
        if(userOptional.isEmpty()){
            throw new BadRequestException("존재하지 않는 유저입니다.");
        }
        User user = userOptional.get();
        String paper="jobPaper.pdf";
        try {
            //uuid로 검색
            String filePath = dst+user.getPaperPath();
            System.out.println("r갸갸갸갹"+filePath);

            //저장된 디렉토리 위치+파일 이름
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename="+paper);

            //내가 보낼 파일의 이름 filename으로 설정해서 전송
            java.io.File file = new java.io.File(filePath);
            response.setContentLengthLong(file.length());
            //파일 객체 생성 -> 리소스 누수
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
            ObjectMapper objectMapper = new ObjectMapper();

            // JSON 객체 생성
            ObjectNode json = objectMapper.createObjectNode();
            json.put("version", "V2");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", System.currentTimeMillis());

            // 이미지 객체 생성
            ObjectNode image = objectMapper.createObjectNode();
            image.put("format", multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")+1));
            image.put("data", Base64.encodeBase64String(multipartFile.getBytes()));
            image.put("name", "StarLightCompany");

            // 이미지 배열 생성 및 추가
            ArrayNode images = objectMapper.createArrayNode();
            images.add(image);
            json.set("images", images);

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

        Optional<User> userOptional = userService.findByUsername(myName);
        Optional<JobOffer> jobOfferOptional = jobOfferService.getJobOfferById(userApplyRequest.getJobOfferId());
        if(userOptional.isEmpty() || jobOfferOptional.isEmpty()){
            throw new BadRequestException("존재하지 않는 사용자 혹은 구인공고입니다.");
        }
        User user = userOptional.get();

        JobOffer jobOffer = jobOfferOptional.get();

        try{
            File dstFile = new File(dst);
            if(!dstFile.getParentFile().exists()){
                throw new BadRequestException("존재하지 않는 사용자입니다.");
            }
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

    @Override
    public boolean fileCheck(MultipartFile multipartFile) {
        //확장자
        String ext=multipartFile.getOriginalFilename().toLowerCase();

        byte[] pngsignature=new byte[]{(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47, (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A};
        byte[] jpegsignature=new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE8};
        byte[] jpgsignature=new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0};
        try {
            byte[] bytes = multipartFile.getBytes();
            byte[] header8 = Arrays.copyOfRange(bytes, 0, 8);
            byte[] header4 = Arrays.copyOfRange(bytes, 0, 4);
            if(false){return false;}
            if (ext.endsWith(".jpg") && Arrays.equals(header4, jpgsignature)) {return true;}
            else if (ext.endsWith(".jpeg") && Arrays.equals(header4, jpegsignature)) {return true;}
            else if (ext.endsWith(".png") && Arrays.equals(header8, pngsignature)) { return true;}
            else{return false;}
        }

        catch(IOException e){
            throw new BadRequestException("잘못된 접근입니다.");

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






