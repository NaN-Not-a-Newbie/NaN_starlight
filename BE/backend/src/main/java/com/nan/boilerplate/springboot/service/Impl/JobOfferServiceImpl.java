package com.nan.boilerplate.springboot.service.Impl;

import com.nan.boilerplate.springboot.model.*;
import com.nan.boilerplate.springboot.repository.CompanyRepository;
import com.nan.boilerplate.springboot.repository.JobOfferRepository;
import com.nan.boilerplate.springboot.security.dto.JobOfferRequest;
import com.nan.boilerplate.springboot.security.dto.JobOfferResponse;
import com.nan.boilerplate.springboot.security.dto.JobOfferSimpleResponse;
import com.nan.boilerplate.springboot.security.jwt.JwtTokenManager;
import com.nan.boilerplate.springboot.security.service.UserDetailsServiceImpl;
import com.nan.boilerplate.springboot.security.service.UserService;
import com.nan.boilerplate.springboot.security.utils.SecurityConstants;
import com.nan.boilerplate.springboot.service.JobOfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.webjars.NotFoundException;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobOfferServiceImpl implements JobOfferService {
    private final JobOfferRepository jobOfferRepository;
    private final CompanyRepository companyRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final UserService userService;
    private final JwtTokenManager jwtTokenManager;
    @Value("${API-KEY.JobOfferApi}")
    String jobOfferApi;
    @Value("${API-KEY.JobOfferKey}")
    String jobOfferKey;

    @Override
    public List<JobOfferSimpleResponse> getAllJobOffers() {
        List<JobOffer> offers = jobOfferRepository.findAll();
        List<JobOfferSimpleResponse> offersResponses = new ArrayList<>();
        for (JobOffer offer : offers) {
            offersResponses.add(JobOfferSimpleResponse.builder()
                    .id(offer.getId())
                    .title(offer.getTitle())
                    .companyName(offer.getCompany().getCompanyName())
                    .build());
        }
        return offersResponses;
    }

    @Override
    public Optional<JobOffer> getJobOfferById(long id) {
        return jobOfferRepository.findById(id);
    }

    @Override
    public Long addJobOffer(JobOfferRequest jobOfferRequest) {
//        User writer = userService.findByUsername(SecurityConstants.getAuthenticatedUsername());
        JobOffer jobOffer = JobOffer.builder()
                .title(jobOfferRequest.getTitle())
                .body(jobOfferRequest.getBody())
                .career(jobOfferRequest.getCareer())
                .location(jobOfferRequest.getLocation())
                .company(companyRepository.findByUsername(SecurityConstants.getAuthenticatedUsername()).get())
                .salary(jobOfferRequest.getSalary())
                .salaryType(jobOfferRequest.getSalaryType())
                .education(jobOfferRequest.getEducation())
                .envEyesight(jobOfferRequest.getEnvEyesight())
                .envhandWork(jobOfferRequest.getEnvhandWork())
                .envLiftPower(jobOfferRequest.getEnvLiftPower())
                .envBothHands(jobOfferRequest.getEnvBothHands())
                .envStndWalk(jobOfferRequest.getEnvStndWalk())
                .envLstnTalk(jobOfferRequest.getEnvLstnTalk())
                .build();

        return jobOfferRepository.save(jobOffer).getId();
    }

    @Override
    public JobOfferSimpleResponse updateJobOffer(Long id, JobOfferRequest jobOfferRequest) {
        String myName = SecurityConstants.getAuthenticatedUsername(); // 로그인 된 계정의 username
        String author = jobOfferRepository.getReferenceById(id).getCompany().getUsername(); // 글 작성자

        if (jobOfferRepository.existsById(id) && author.equals(myName)) {
            JobOffer existJobOffer = jobOfferRepository.getReferenceById(id);
            existJobOffer.setTitle(jobOfferRequest.getTitle());
            existJobOffer.setLocation(jobOfferRequest.getLocation());
            existJobOffer.setEducation(jobOfferRequest.getEducation());
            existJobOffer.setSalaryType(jobOfferRequest.getSalaryType());
            existJobOffer.setSalary(jobOfferRequest.getSalary());
            existJobOffer.setCareer(jobOfferRequest.getCareer());
            existJobOffer.setBody(jobOfferRequest.getBody());
            existJobOffer.setEnvEyesight(jobOfferRequest.getEnvEyesight());
            existJobOffer.setEnvhandWork(jobOfferRequest.getEnvhandWork());
            existJobOffer.setEnvLiftPower(jobOfferRequest.getEnvLiftPower());
            existJobOffer.setEnvStndWalk(jobOfferRequest.getEnvStndWalk());
            existJobOffer.setEnvBothHands(jobOfferRequest.getEnvBothHands());
            existJobOffer.setEnvLstnTalk(jobOfferRequest.getEnvLstnTalk());
            return JobOfferSimpleResponse.builder().title(jobOfferRepository.save(existJobOffer).getTitle()).build();

        } else {
            if (!jobOfferRepository.existsById(id)) {
                throw new NotFoundException("not exist JobOffer with id: {id}");
            } else {
                throw new NotFoundException("not exist JobOffer with id: {id}");
            }
        }
    }

    @Override
    public void deleteJobOffer(long id) {
        jobOfferRepository.deleteById(id);
    }

    @Override
    public List<String> getOptialJobOffers() {
        try {
            // HttpClient 생성
            HttpClient client = HttpClient.newHttpClient();

            // GET 요청 생성
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://apis.data.go.kr/B552583/job/job_list_env?" + jobOfferKey + "&pageNo=1&numOfRows=100"))
                    .GET()
                    .build();

            // 요청 보내고 응답 받기
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // 문자열을 InputSource로 변환하여 파싱합니다.
            Document document = builder.parse(new InputSource(new StringReader(response.body())));

            // 루트 엘리먼트를 가져옵니다.
            // 루트 엘리먼트 가져오기
            Element root = document.getDocumentElement();

            // 'item' 태그를 가진 모든 엘리먼트 가져오기
            NodeList itemList = root.getElementsByTagName("item");

            for (int i = 0; i < itemList.getLength(); i++) {
                Node itemNode = itemList.item(i);

                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) itemNode;

                    // 각 태그의 값을 가져옵니다.
                    String empType = getTextContent(itemElement,"empType");
                    String busplaName = getTextContent(itemElement, "busplaName");
                    String salaryType = getTextContent(itemElement, "salaryType");
                    String envBothHands = getTextContent(itemElement, "envBothHands");
                    String envEyesight = getTextContent(itemElement, "envEyesight");
                    String envStndWalk = getTextContent(itemElement, "envStndWalk");
                    String envLstnTalk = getTextContent(itemElement, "envLstnTalk");
                    String envLiftPower = getTextContent(itemElement, "envLiftPower");
                    String education = getTextContent(itemElement, "reqEduc");
                    String envHandWork = getTextContent(itemElement, "envHandWork");
                    String career = getTextContent(itemElement, "reqCareer");
                    String location = getTextContent(itemElement, "compAddr");
                    String salary = getTextContent(itemElement, "salary");

                    EnvBothHands[] envBothHands1=EnvBothHands.values();
                    EnvEyesight[] envEyesight1=EnvEyesight.values();
                    EnvHandWork[] envHandWork1=EnvHandWork.values();
                    EnvLiftPower[] envLiftPower1=EnvLiftPower.values();
                    EnvStndWalk[] envStndWalk1=EnvStndWalk.values();
                    EnvLstnTalk[] envLstnTalk1=EnvLstnTalk.values();
                    SalaryType[] salaryType1=SalaryType.values();
                    Education[] education1=Education.values();

                    EnvBothHands envBothHands2=switch (envBothHands) {
                        case "한손작업 가능"->envBothHands1[0];
                        case "한손보조작업 가능"->envBothHands1[1];
                        case "양손작업 가능"->envBothHands1[2];
                        default -> envBothHands1[3];

                    };
                    EnvEyesight envEyesight2=switch (envEyesight) {
                        case "아주 작은 글씨를 읽을 수 있음"->envEyesight1[0];
                        case "일상적 활동 가능"->envEyesight1[1];
                        case "비교적 큰 인쇄물을 읽을 수 있음"->envEyesight1[2];
                        default -> envEyesight1[3];
                    };
                    EnvStndWalk envStndWalk2=switch (envStndWalk) {
                        case "오랫동안 가능"->envStndWalk1[0];
                        case "서거나 걷는 일 어려움"->envStndWalk1[1];
                        case "일부 서서하는 작업 가능"->envStndWalk1[2];
                        default -> envStndWalk1[3];
                    };
                    EnvLiftPower envLiftPower2 = switch (envLiftPower) {
                        case "5Kg 이내의 물건을 다룰 수 있음"->envLiftPower1[0];
                        case "5~20Kg 이내의 물건을 다룰 수 있음"->envLiftPower1[1];
                        case "20Kg 이상의 물건을 다룰 수 있음"->envLiftPower1[2];
                        default -> envLiftPower1[3];
                    };
                    EnvLstnTalk envLstnTalk2= switch (envLstnTalk){
                        case "간단한 듣고 말하기 가능"->envLstnTalk1[0];
                        case "듣고 말하는 작업 어려움"->envLstnTalk1[1];
                        case "듣고 말하기에 어려움 없음"->envLstnTalk1[2];
                        default -> envLstnTalk1[3];
                    };
                    EnvHandWork envHandWork2= switch (envHandWork) {
                        case "큰 물품 조립가능"->envHandWork1[0];
                        case "작은 물품 조립가능"->envHandWork1[1];
                        case "정밀한 작업가능"->envHandWork1[2];
                        default -> envHandWork1[3];

                    };
                    Education education2=switch (education){
                        case "초졸"->education1[0];
                        case "중졸"->education1[1];
                        case "고졸"->education1[2];
                        case "대졸"->education1[3];
                        case "초대졸"->education1[3];
                        case "석사"->education1[4];
                        case "박사"->education1[5];
                        default -> education1[6];
                    };
                    SalaryType salaryType2= switch (salaryType){
                        case "시급"->salaryType1[0];
                        case "주급"->salaryType1[1];
                        case "월급"->salaryType1[2];
                        case "연봉"->salaryType1[3];
                        default -> salaryType1[3];
                    };

                    JobOfferRequest jobOfferRequest=JobOfferRequest.builder()
                            .title(busplaName+" "+empType+" 채용")
                            .Body(busplaName+" "+empType+" 채용")
                            .career(career.equals("무관") ? 0 :
                                    (career.contains("년") ? Long.parseLong(career.substring(0, career.indexOf("년")).trim()) : 0))
                            .education(education2)
                            .envBothHands(envBothHands2)
                            .envEyesight(envEyesight2)
                            .envhandWork(envHandWork2)
                            .envLiftPower(envLiftPower2)
                            .envLstnTalk(envLstnTalk2)
                            .envStndWalk(envStndWalk2)
                            .location(location)
                            .salary(Long.parseLong(salary.replace(",","")))
                            .salaryType(salaryType2)
                            .build();
                    // 결과 출력
                    System.out.println("Business Place Name: " + busplaName);
                    System.out.println("Salary Type: " + salaryType);
                    System.out.println("Environment Both Hands: " + envBothHands);
                    System.out.println("Environment Eyesight: " + envEyesight);
                    System.out.println("Environment Stand Walk: " + envStndWalk);
                    System.out.println("Environment Listen Talk: " + envLstnTalk);
                    System.out.println("Environment Lift Power: " + envLiftPower);
                    System.out.println("Education: " + education);
                    System.out.println("Environment Hand Work: " + envHandWork);
                    System.out.println("Career: " + career);
                    System.out.println("Location: " + location);
                    System.out.println("Salary Class Name: " + itemElement.getElementsByTagName("salary").item(0).getClass().getName());
                    System.out.println("-------------------------");
                    addJobOffer(jobOfferRequest);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }
    private String getTextContent(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    @Override
    public boolean existsJobOffer(long id) {
        return false;
    }

    @Override
    public List<JobOfferSimpleResponse> initialJobOffer() {
        String myName = SecurityConstants.getAuthenticatedUsername();
        User user = userService.findByUsername(myName);
        EnvHandWork envHandWork=user.getEnvhandWork();
        EnvBothHands envBothHands=user.getEnvBothHands();
        EnvEyesight envEyesight=user.getEnvEyesight();
        EnvLiftPower envLiftPower=user.getEnvLiftPower();
        EnvLstnTalk envLstnTalk=user.getEnvLstnTalk();
        EnvStndWalk envStndWalk=user.getEnvStndWalk();

        List<JobOffer> jobOffers=jobOfferRepository.findByEnvhandWorkOrEnvBothHandsOrEnvLiftPowerOrEnvLstnTalkOrEnvStndWalkOrEnvEyesight(
                envHandWork,envBothHands,envLiftPower,envLstnTalk,envStndWalk,envEyesight);
        System.out.println(jobOffers.size());
        System.out.println(jobOffers.get(0));
        List<JobOfferSimpleResponse> offersResponses = new ArrayList<>();
        for(JobOffer offer:jobOffers){
            offersResponses.add(JobOfferSimpleResponse.builder()
                    .id(offer.getId())
                    .title(offer.getTitle())
                    .companyName(offer.getCompany().getCompanyName())
                    .build());
        }
        return offersResponses;
    }
}
