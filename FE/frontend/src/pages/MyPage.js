import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Container, Typography, Table, TableBody, TableCell, TableContainer, TableRow, Paper, Button, Box } from '@mui/material';

const educationMapping = {
    DONTCARE: '학력 무관',
    ELEM: '초졸',
    MIDDLE: '중졸',
    HIGH: '고졸',
    UNIV: '대졸',
    MASTER: '석사',
    DOCTOR: '박사'
};

const disabilityConditionMapping = {
    envEyesight: {
        VERYSMALLCHAR: '아주 작은 글씨',
        ADL: '일상적 활동',
        BIGHAR: '비교적 큰 인쇄물',
        DONTCARE: '무관'
    },
    envBothHands: {
        ONE: '한손작업 가능',
        SUBHAND: '한손보조작업 가능',
        BOTHAND: '양손작업 가능',
        DONTCARE: '무관'
    },
    envhandWork: {
        BIG: '큰 물품 조립가능',
        SMALL: '작은 물품 조립가능',
        ACCURATE: '정밀한 작업 가능',
        DONTCARE: '무관'
    },
    envLiftPower: {
        UNDER5: '5Kg 이내의 물건',
        UNDER20: '5~20Kg의 물건',
        OVER20: '20Kg 이상의 물건',
        DONTCARE: '무관'
    },
    envStndWalk: {
        LONG: '오랫동안 가능',
        HARD: '서거나 걷는 일이 어려움',
        PART: '일부 서서하는 작업 가능',
        DONTCARE: '무관'
    },
    envLstnTalk: {
        HARD: '듣고 말하는 작업 어려움',
        NOPROBLEM: '듣고 말하기에 어려움 없음',
        SIMPLETALLK: '간단한 듣고 말하기 가능',
        DONTCARE: '무관'
    }
};

const MyPage = () => {
    const [userData, setUserData] = useState(null);
    const [role, setRole] = useState('');
    const [jobOffers, setJobOffers] = useState([]); // 회사 계정으로 작성한 공고 목록 저장
    const [loading, setLoading] = useState(true); // 로딩 상태 추가

    useEffect(() => {
        const fetchData = async () => {
            const token = localStorage.getItem('token');
            const config = {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            };

            try {
                const response = await axios.get('http://localhost:8080/me', config);
                setRole(response.data.role);
                setUserData(response.data.information);

                if (response.data.role === 'COMPANY') {
                    const jobOffersResponse = await axios.get('http://localhost:8080/me/jobOffers', config);
                    setJobOffers(jobOffersResponse.data); // 공고 목록 저장
                }
            } catch (error) {
                console.error('데이터를 가져오는 중 오류가 발생했습니다.', error);
            }

            setLoading(false); // 로딩 상태 해제
        };

        fetchData();
    }, []);

    const handleDownloadResume = async () => {
        try {
            const token = localStorage.getItem('token');
            const config = {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
                responseType: 'blob' // 다운로드 받기 위해 responseType을 blob으로 설정
            };
    
            const response = await axios.get('http://localhost:8080/userApply/downloadContract', config);
    
            let filename = 'resume.pdf'; // 기본 파일 이름 설정
            const contentDisposition = response.headers['content-disposition'];
            
            if (contentDisposition && contentDisposition.indexOf('filename=') !== -1) {
                filename = contentDisposition.split('filename=')[1].replace(/"/g, '');
            }
    
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', filename);
            document.body.appendChild(link);
            link.click();
        } catch (error) {
            console.error('이력서 다운로드 중 오류가 발생했습니다.', error);
        }
    };
    
    if (loading) {
        return <Typography>로딩 중...</Typography>;
    }

    return (
        <Container maxWidth="md" sx={{ mt: 4 }}>
            {role === 'COMPANY' ? (
                <>
                    <h2>회사 정보</h2>
                    <TableContainer component={Paper} sx={{ boxShadow: 'none' }}>
                        <Table>
                            <TableBody>
                                <TableRow>
                                    <TableCell>아이디</TableCell>
                                    <TableCell>{userData.username}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>회사명</TableCell>
                                    <TableCell>{userData.companyName}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>사업자 등록번호</TableCell>
                                    <TableCell>{userData.companyRegistrationNumber}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>전화번호</TableCell>
                                    <TableCell>{userData.phoneNum}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>회사 주소</TableCell>
                                    <TableCell>{userData.companyAddress}</TableCell>
                                </TableRow>
                            </TableBody>
                        </Table>
                    </TableContainer>

                    <h2>작성한 공고 목록</h2>
                    {jobOffers.length > 0 ? (
                        <TableContainer component={Paper} sx={{ boxShadow: 'none' }}>
                            <Table>
                                <TableBody>
                                    <TableRow>
                                        <TableCell>공고 명</TableCell>
                                        <TableCell>위치</TableCell>
                                        {/* <TableCell>급여</TableCell> */}
                                        <TableCell>마감일</TableCell>
                                        <TableCell>경력</TableCell>
                                    </TableRow>
                                    {jobOffers.map((offer) => (
                                        <TableRow key={offer.id}>
                                            <TableCell>{offer.title}</TableCell>
                                            <TableCell>{offer.location}</TableCell>
                                            {/* <TableCell>{offer.salary} {offer.salaryType}</TableCell> */}
                                            <TableCell>{offer.deadLine}</TableCell>
                                            <TableCell>{offer.career === 0 ? '신입' : `${offer.career}년`}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    ) : (
                        <Typography>작성한 공고가 없습니다.</Typography>
                    )}
                </>
            ) : (
                <>
                    <h2>사용자 정보</h2>
                    <TableContainer component={Paper} sx={{ boxShadow: 'none' }}>
                        <Table>
                            <TableBody>
                                <TableRow>
                                    <TableCell>아이디</TableCell>
                                    <TableCell>{userData.username}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>이름</TableCell>
                                    <TableCell>{userData.name}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>생년월일</TableCell>
                                    <TableCell>{userData.birthday}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>전화번호</TableCell>
                                    <TableCell>{userData.phoneNum}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>시력</TableCell>
                                    <TableCell>{disabilityConditionMapping.envEyesight[userData.envEyesight]}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>양손 사용 능력</TableCell>
                                    <TableCell>{disabilityConditionMapping.envBothHands[userData.envBothHands]}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>손작업 정확도</TableCell>
                                    <TableCell>{disabilityConditionMapping.envhandWork[userData.envhandWork]}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>들기 힘 (kg)</TableCell>
                                    <TableCell>{disabilityConditionMapping.envLiftPower[userData.envLiftPower]}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>서있기/걷기 능력</TableCell>
                                    <TableCell>{disabilityConditionMapping.envStndWalk[userData.envStndWalk]}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>듣기/말하기 능력</TableCell>
                                    <TableCell>{disabilityConditionMapping.envLstnTalk[userData.envLstnTalk]}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>최종 학력</TableCell>
                                    <TableCell>{educationMapping[userData.education]}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>성별</TableCell>
                                    <TableCell>{userData.isMale ? '남성' : '여성'}</TableCell>
                                </TableRow>
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <Box sx={{ textAlign: 'center', mt: 4 }}>
                        <Button variant="contained" color="primary" onClick={handleDownloadResume}>
                            근로 계약서 다운로드
                        </Button>
                    </Box>
                </>
            )}
        </Container>
    );
};

export default MyPage;