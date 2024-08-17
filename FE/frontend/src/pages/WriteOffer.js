import React, { useState } from 'react';
import { TextField, Button, Container, Typography, Box, FormControl, InputLabel, Select, MenuItem } from '@mui/material';
import axios from 'axios';

const WriteOffer = () => {
    const [formData, setFormData] = useState({
        title: '',
        salary: '',
        location: '',
        career: '',
        cntctNo: '',
        salaryType: 'TIME',
        envEyesight: 'VERYSMALLCHAR',
        envBothHands: 'ONE',
        envhandWork: 'BIG',
        envLiftPower: 'UNDER5',
        envStndWalk: 'LONG',
        envLstnTalk: 'HARD',
        education: 'ELEM',
        deadLine: '',
        body: ''
    });
    const educationMapping = {
        ELEM: '초졸',
        MIDDLE: '중졸',
        HIGH: '고졸',
        UNIV: '대졸',
        MASTER: '석사',
        DOCTOR: '박사'
      };
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem('token'); // JWT 토큰 가져오기
            const config = {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            };
            const response = await axios.post('http://localhost:8080/jobOffer', formData, config);
            console.log('공고가 성공적으로 등록되었습니다:', response.data);
        } catch (error) {
            console.error('공고 등록 중 오류가 발생했습니다:', error);
        }
    };

    return (
        <Container component="main" maxWidth="md">
           <h2>공고 작성</h2>
            <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    id="title"
                    label="공고 제목"
                    name="title"
                    value={formData.title}
                    onChange={handleChange}
                />
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    id="salary"
                    label="급여"
                    name="salary"
                    type="number"
                    value={formData.salary}
                    onChange={handleChange}
                />
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    id="location"
                    label="근무지"
                    name="location"
                    value={formData.location}
                    onChange={handleChange}
                />
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    id="career"
                    label="경력 (년)"
                    name="career"
                    type="number"
                    value={formData.career}
                    onChange={handleChange}
                />
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    id="cntctNo"
                    label="연락처"
                    name="cntctNo"
                    value={formData.cntctNo}
                    onChange={handleChange}
                />
                <FormControl variant="outlined" margin="normal" required fullWidth>
                    <InputLabel id="salaryType-label">급여 유형</InputLabel>
                    <Select
                        labelId="salaryType-label"
                        id="salaryType"
                        name="salaryType"
                        value={formData.salaryType}
                        onChange={handleChange}
                        label="급여 유형"
                    >
                        <MenuItem value="TIME">시급</MenuItem>
                        <MenuItem value="WEEK">주급</MenuItem>
                        <MenuItem value="MONTH">월급</MenuItem>
                        <MenuItem value="YEAR">연봉</MenuItem>
                    </Select>
                </FormControl>
                <FormControl variant="outlined" margin="normal" required fullWidth>
              <InputLabel id="education-label">최종 학력</InputLabel>
              <Select
                labelId="education-label"
                id="education"
                name="education"
                value={formData.education}
                onChange={handleChange}
                label="최종 학력"
              >
                {Object.keys(educationMapping).map((key) => (
                  <MenuItem key={key} value={key}>
                    {educationMapping[key]}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
                <FormControl variant="outlined" margin="normal" required fullWidth>
                    <InputLabel id="envEyesight-label">시력</InputLabel>
                    <Select
                        labelId="envEyesight-label"
                        id="envEyesight"
                        name="envEyesight"
                        value={formData.envEyesight}
                        onChange={handleChange}
                        label="시력"
                    >
                        <MenuItem value="VERYSMALLCHAR">아주 작은 글씨</MenuItem>
                        <MenuItem value="ADL">일상적 활동</MenuItem>
                        <MenuItem value="BIGHAR">비교적 큰 인쇄물</MenuItem>
                        <MenuItem value="DONTCARE">무관</MenuItem>
                    </Select>
                </FormControl>

                <FormControl variant="outlined" margin="normal" required fullWidth>
                    <InputLabel id="envBothHands-label">양손 사용 능력</InputLabel>
                    <Select
                        labelId="envBothHands-label"
                        id="envBothHands"
                        name="envBothHands"
                        value={formData.envBothHands}
                        onChange={handleChange}
                        label="양손 사용 능력"
                    >
                        <MenuItem value="ONE">한손작업 가능</MenuItem>
                        <MenuItem value="SUBHAND">한손보조작업 가능</MenuItem>
                        <MenuItem value="BOTHAND">양손작업 가능</MenuItem>
                        <MenuItem value="DONTCARE">무관</MenuItem>
                    </Select>
                </FormControl>

                <FormControl variant="outlined" margin="normal" required fullWidth>
                    <InputLabel id="envhandWork-label">손작업 정확도</InputLabel>
                    <Select
                        labelId="envhandWork-label"
                        id="envhandWork"
                        name="envhandWork"
                        value={formData.envhandWork}
                        onChange={handleChange}
                        label="손작업 정확도"
                    >
                        <MenuItem value="BIG">큰 물품 조립가능</MenuItem>
                        <MenuItem value="SMALL">작은 물품 조립가능</MenuItem>
                        <MenuItem value="ACCURATE">정밀한 작업 가능</MenuItem>
                        <MenuItem value="DONTCARE">무관</MenuItem>
                    </Select>
                </FormControl>

                <FormControl variant="outlined" margin="normal" required fullWidth>
                    <InputLabel id="envLiftPower-label">들기 힘 (kg)</InputLabel>
                    <Select
                        labelId="envLiftPower-label"
                        id="envLiftPower"
                        name="envLiftPower"
                        value={formData.envLiftPower}
                        onChange={handleChange}
                        label="들기 힘 (kg)"
                    >
                        <MenuItem value="UNDER5">5Kg 이내의 물건</MenuItem>
                        <MenuItem value="UNDER20">5~20Kg의 물건</MenuItem>
                        <MenuItem value="OVER20">20Kg 이상의 물건</MenuItem>
                        <MenuItem value="DONTCARE">무관</MenuItem>
                    </Select>
                </FormControl>

                <FormControl variant="outlined" margin="normal" required fullWidth>
                    <InputLabel id="envStndWalk-label">서있기/걷기 능력</InputLabel>
                    <Select
                        labelId="envStndWalk-label"
                        id="envStndWalk"
                        name="envStndWalk"
                        value={formData.envStndWalk}
                        onChange={handleChange}
                        label="서있기/걷기 능력"
                    >
                        <MenuItem value="LONG">오랫동안 가능</MenuItem>
                        <MenuItem value="HARD">서거나 걷는 일이 어려움</MenuItem>
                        <MenuItem value="PART">일부 서서하는 작업 가능</MenuItem>
                        <MenuItem value="DONTCARE">무관</MenuItem>
                    </Select>
                </FormControl>

                <FormControl variant="outlined" margin="normal" required fullWidth>
                    <InputLabel id="envLstnTalk-label">듣기/말하기 능력</InputLabel>
                    <Select
                        labelId="envLstnTalk-label"
                        id="envLstnTalk"
                        name="envLstnTalk"
                        value={formData.envLstnTalk}
                        onChange={handleChange}
                        label="듣기/말하기 능력"
                    >
                        <MenuItem value="HARD">듣고 말하는 작업 어려움</MenuItem>
                        <MenuItem value="NOPROBLEM">듣고 말하기에 어려움 없음</MenuItem>
                        <MenuItem value="SIMPLETALLK">간단한 듣고 말하기 가능</MenuItem>
                        <MenuItem value="DONTCARE">무관</MenuItem>
                    </Select>
                </FormControl>

                <FormControl variant="outlined" margin="normal" required fullWidth>
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        id="deadLine"
                        label="마감일"
                        name="deadLine"
                        type="date"
                        InputLabelProps={{ shrink: true }}
                        value={formData.deadLine}
                        onChange={handleChange}
                    />
                </FormControl>

                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    multiline
                    rows={4}
                    id="body"
                    label="공고 내용"
                    name="body"
                    value={formData.body}
                    onChange={handleChange}
                />

                <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 2 }}>
                    <Button variant="contained" color="primary" type="submit">
                        공고 등록
                    </Button>
                </Box>
            </Box>
        </Container>
    );
};

export default WriteOffer;
