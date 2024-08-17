import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Container, Typography, CircularProgress, Button, Table, TableBody, TableCell, TableContainer, TableRow, Paper, Box } from '@mui/material';

const educationMapping = {
    DONTCARE: '학력 무관',
    ELEM: '초졸',
    MIDDLE: '중졸',
    HIGH: '고졸',
    UNIV: '대졸',
    MASTER: '석사',
    DOCTOR: '박사'
};

const salaryTypeMapping = {
    TIME: '시급',
    WEEK: '주급',
    MONTH: '월급',
    YEAR: '연봉'
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

const formatSalary = (salary) => {
    return salary.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
};

const JobDetail = () => {
    const { id } = useParams();
    const [jobDetail, setJobDetail] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchJobDetail = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/jobOffer/${id}`);
                setJobDetail(response.data);
            } catch (error) {
                console.error('Error fetching job details:', error);
            }
            setLoading(false);
        };

        fetchJobDetail();
    }, [id]);

    const handleApplyClick = async () => {
        const token = localStorage.getItem('token');
        const config = {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        };

        const applyData = {
            hire: true,
            jobOfferId: id,  // 현재 게시글의 ID
        };

        try {
            await axios.post('http://localhost:8080/userApply/makeContract', applyData, config);
            navigate('/jobs');  // 지원 후 목록 페이지로 이동
        } catch (error) {

        }
    };

    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
                <CircularProgress />
            </Box>
        );
    }

    return (
        <Container maxWidth="md" sx={{ mt: 4 }}>
            {jobDetail ? (
                <>
                    <h2>{jobDetail.title}</h2>
                    <TableContainer component={Paper} sx={{ boxShadow: 'none' }}>
                        <Table>
                            <TableBody>
                                <TableRow>
                                    <TableCell component="th" scope="row">회사명</TableCell>
                                    <TableCell>{jobDetail.companyName}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell component="th" scope="row">급여</TableCell>
                                    <TableCell>{salaryTypeMapping[jobDetail.salaryType]} {formatSalary(jobDetail.salary)}원</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell component="th" scope="row">경력</TableCell>
                                    <TableCell>{jobDetail.career ? jobDetail.career + "년" : "신입"}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell component="th" scope="row">학력</TableCell>
                                    <TableCell>{educationMapping[jobDetail.education]}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell component="th" scope="row">위치</TableCell>
                                    <TableCell>{jobDetail.location}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell component="th" scope="row">마감일</TableCell>
                                    <TableCell>{jobDetail.deadLine}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell component="th" scope="row">연락처</TableCell>
                                    <TableCell>{jobDetail.cntctNo}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell component="th" scope="row">상세 설명</TableCell>
                                    <TableCell>{jobDetail.body}</TableCell>
                                </TableRow>
                            </TableBody>
                        </Table>
                    </TableContainer>

                    <h2>근무 조건</h2>
                    <TableContainer component={Paper} sx={{ boxShadow: 'none' }}>
                        <Table>
                            <TableBody>
                                <TableRow>
                                    <TableCell component="th" scope="row">시력</TableCell>
                                    <TableCell>{disabilityConditionMapping.envEyesight[jobDetail.envEyesight]}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell component="th" scope="row">양손 사용 능력</TableCell>
                                    <TableCell>{disabilityConditionMapping.envBothHands[jobDetail.envBothHands]}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell component="th" scope="row">손작업 정확도</TableCell>
                                    <TableCell>{disabilityConditionMapping.envhandWork[jobDetail.envhandWork]}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell component="th" scope="row">들기 힘 (kg)</TableCell>
                                    <TableCell>{disabilityConditionMapping.envLiftPower[jobDetail.envLiftPower]}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell component="th" scope="row">서있기/걷기 능력</TableCell>
                                    <TableCell>{disabilityConditionMapping.envStndWalk[jobDetail.envStndWalk]}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell component="th" scope="row">듣기/말하기 능력</TableCell>
                                    <TableCell>{disabilityConditionMapping.envLstnTalk[jobDetail.envLstnTalk]}</TableCell>
                                </TableRow>
                            </TableBody>
                        </Table>
                    </TableContainer>

                    <Button 
                        variant="contained" 
                        color="primary" 
                        onClick={handleApplyClick} 
                        sx={{ mt: 3, float: 'center' }}
                    >
                        지원하기
                    </Button>
                    <br/>
                    <br/>
                    <br/>

                    <br/>
                    <br/>
                </>
            ) : (
                <Typography variant="h6" color="error">
                    해당 채용 정보를 찾을 수 없습니다.
                </Typography>
            )}
        </Container>
    );
};

export default JobDetail;
