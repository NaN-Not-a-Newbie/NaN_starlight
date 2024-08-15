package com.nan.boilerplate.springboot.service.Impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.security.dto.UserApplyRequest;
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

@Service
@RequiredArgsConstructor
public class FileServceImpl implements FileService {
    private final UserService userService;
    private final JobOfferService jobOfferService;
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
            paragraphSalary.setFixedPosition(1, 170, 560, 400);
            paragraphSalaryType.setFixedPosition(1, 120, 560, 300);

            document.add(paragraphName);
            document.add(paragraphcompanyName);
            document.add(paragraphLocation);
            document.add(paragraphSalary);
            document.add(paragraphSalaryType);

            String imagePath = "BE/backend/src/main/resources/static/sign/2f0da2bb-9950-48bd-a745-13016e744004_page0.png"; // 삽입할 이미지 경로
            ImageData imageData = ImageDataFactory.create(imagePath);
            com.itextpdf.layout.element.Image image = new Image(imageData);

            // 이미지 크기 설정 (예: 이미지의 너비를 200포인트, 높이를 100포인트로 설정)
            image.scaleToFit(200, 100);

            // 이미지의 위치 설정 (예: x=300, y=500 위치에 삽입)
            image.setFixedPosition(1, 300, 0);

            // 이미지를 문서에 추가
            document.add(image);


            document.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void backgroundCutout(InputStream inputPdfStream) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String dst = "BE/backend/src/main/resources/static/sign/" + uuid + ".png";

        try (PDDocument document = Loader.loadPDF(inputPdfStream.readAllBytes())) {
            File dstFile = new File(dst);
            dstFile.getParentFile().mkdirs(); // Ensure the directory exists

            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int pageNum = 0; pageNum < document.getNumberOfPages(); pageNum++) {
                // Convert the PDF page to a BufferedImage
                BufferedImage image = pdfRenderer.renderImageWithDPI(pageNum, 300, ImageType.RGB);
                BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

                // Iterate through each pixel
                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        int rgb = image.getRGB(x, y);
                        Color color = new Color(rgb, true);
                        if (color.getRed() == 255 && color.getGreen() == 255 && color.getBlue() == 255) {
                            // Set white color to transparent
                            newImage.setRGB(x, y, 0x00FFFFFF & rgb);
                        } else {
                            // Keep the original color
                            newImage.setRGB(x, y, rgb);
                        }
                    }
                }

                // Save the modified image to a PNG file
                String pageDst = dst.replace(".png", "_page" + pageNum + ".png");
                File outputImageFile = new File(pageDst);
                ImageIO.write(newImage, "PNG", outputImageFile);
            }
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

