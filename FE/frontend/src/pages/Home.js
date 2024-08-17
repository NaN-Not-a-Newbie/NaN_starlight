import React, { useEffect, useState, useRef } from 'react';
import { Container, CircularProgress, FilledInput, InputAdornment, IconButton, Box, Accordion, AccordionSummary, AccordionDetails, Grid, Select, MenuItem, FormControl, InputLabel, Button, Stack } from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import SearchIcon from '@mui/icons-material/Search';
import axios from 'axios';
import CardList from '../components/CardList';
import { useLocation } from 'react-router-dom';

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
  envHandWork: {
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

function Home() {
  const [jobOffers, setJobOffers] = useState([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const [filters, setFilters] = useState({
    title: '',
    education: '',
    envEyesight: '',
    envHandWork: '',
    envStndWalk: '',
    envLstnTalk: '',
    envLiftPower: '',
    envBothHands: ''
  });
  const observer = useRef();
  const location = useLocation();

  const fetchJobOffers = async () => {
    setLoading(true);
    const queryParams = new URLSearchParams({
      page,
      size: 10,
      sort: 'deadLine',
      ...filters
    });

    // 필터 중 빈 값인 항목은 쿼리에서 제거
    Object.keys(filters).forEach(key => {
      if (!filters[key]) {
        queryParams.delete(key);
      }
    });

    try {
      const response = await axios.get(`http://localhost:8080/jobOffer?${queryParams.toString()}`);
      setJobOffers(prevOffers => [...prevOffers, ...response.data.content]);
      setHasMore(response.data.content.length > 0);
    } catch (error) {
      console.error('Error fetching job offers:', error);
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchJobOffers();
  }, [page]);

  const lastJobOfferElementRef = useRef();
  useEffect(() => {
    if (loading) return;
    if (observer.current) observer.current.disconnect();
    observer.current = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && hasMore) {
        setPage(prevPage => prevPage + 1);
      }
    });
    if (lastJobOfferElementRef.current) {
      observer.current.observe(lastJobOfferElementRef.current);
    }
  }, [loading, hasMore]);

  const handleSearch = () => {
    setPage(0);
    setJobOffers([]);
    fetchJobOffers();
  };

  const handleFilterChange = (event) => {
    const { name, value } = event.target;
    setFilters(prevFilters => ({ ...prevFilters, [name]: value }));
  };

  return (
    <div>
      {(location.pathname === '/' || location.pathname === '/jobs') && (
        <Box sx={{
          padding: '15px',
          position: 'sticky',
          top: 0,
          zIndex: 1100,
          backgroundColor: 'white',
        }}>
          <FilledInput
            fullWidth
            placeholder="검색"
            value={filters.title}
            onChange={handleFilterChange}
            name="title"
            endAdornment={
              <InputAdornment position="end">
                <IconButton onClick={handleSearch}>
                  <SearchIcon />
                </IconButton>
              </InputAdornment>
            }
            sx={{
              mb: 2,
              borderRadius: '10px',
              border: '0.105rem solid',
              borderColor: 'primary.main',
              backgroundColor: 'transparent',
              '&:hover': {
                backgroundColor: 'transparent',
              },
              '& .MuiFilledInput-input': {
                padding: '10px 12px',
              },
              '&:before': {
                display: 'none',
              },
              '&:after': {
                display: 'none',
              },
            }}
          />

          <Accordion sx={{ mb: 2 }}>
            <AccordionSummary expandIcon={<ExpandMoreIcon />}>
              필터 옵션
            </AccordionSummary>
            <AccordionDetails>
              <Grid container spacing={2}>
                {/* 학력 필터 */}
                <Grid item xs={6} sm={4}>
                  <FormControl fullWidth variant="filled">
                    <InputLabel id="education-label">학력</InputLabel>
                    <Select
                      labelId="education-label"
                      value={filters.education}
                      onChange={handleFilterChange}
                      name="education"
                    >
                      {Object.keys(educationMapping).map(key => (
                        <MenuItem key={key} value={key}>
                          {educationMapping[key]}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>

                {/* 시력 필터 */}
                <Grid item xs={6} sm={4}>
                  <FormControl fullWidth variant="filled">
                    <InputLabel id="envEyesight-label">시력</InputLabel>
                    <Select
                      labelId="envEyesight-label"
                      value={filters.envEyesight}
                      onChange={handleFilterChange}
                      name="envEyesight"
                    >
                      {Object.keys(disabilityConditionMapping.envEyesight).map(key => (
                        <MenuItem key={key} value={key}>
                          {disabilityConditionMapping.envEyesight[key]}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>

                {/* 손작업 정확도 필터 */}
                <Grid item xs={6} sm={4}>
                  <FormControl fullWidth variant="filled">
                    <InputLabel id="envHandWork-label">손작업 정확도</InputLabel>
                    <Select
                      labelId="envHandWork-label"
                      value={filters.envHandWork}
                      onChange={handleFilterChange}
                      name="envHandWork"
                    >
                      {Object.keys(disabilityConditionMapping.envHandWork).map(key => (
                        <MenuItem key={key} value={key}>
                          {disabilityConditionMapping.envHandWork[key]}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>


                {/* 추가 필터 예시: 들기 힘 (kg) 필터 */}
                <Grid item xs={6} sm={4}>
                  <FormControl fullWidth variant="filled">
                    <InputLabel id="envLiftPower-label">들기 힘 (kg)</InputLabel>
                    <Select
                      labelId="envLiftPower-label"
                      value={filters.envLiftPower}
                      onChange={handleFilterChange}
                      name="envLiftPower"
                    >
                      {Object.keys(disabilityConditionMapping.envLiftPower).map(key => (
                        <MenuItem key={key} value={key}>
                          {disabilityConditionMapping.envLiftPower[key]}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>

                {/* 추가 필터 예시: 서있기/걷기 능력 필터 */}
                <Grid item xs={6} sm={4}>
                  <FormControl fullWidth variant="filled">
                    <InputLabel id="envStndWalk-label">서있기/걷기 능력</InputLabel>
                    <Select
                      labelId="envStndWalk-label"
                      value={filters.envStndWalk}
                      onChange={handleFilterChange}
                      name="envStndWalk"
                    >
                      {Object.keys(disabilityConditionMapping.envStndWalk).map(key => (
                        <MenuItem key={key} value={key}>
                          {disabilityConditionMapping.envStndWalk[key]}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>

                {/* 추가 필터 예시: 듣기/말하기 능력 필터 */}
                <Grid item xs={6} sm={4}>
                  <FormControl fullWidth variant="filled">
                    <InputLabel id="envLstnTalk-label">듣기/말하기 능력</InputLabel>
                    <Select
                      labelId="envLstnTalk-label"
                      value={filters.envLstnTalk}
                      onChange={handleFilterChange}
                      name="envLstnTalk"
                    >
                      {Object.keys(disabilityConditionMapping.envLstnTalk).map(key => (
                        <MenuItem key={key} value={key}>
                          {disabilityConditionMapping.envLstnTalk[key]}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>
                {/* 정렬 기준 선택 */}
                <Grid item xs={6} sm={4}>
                  <FormControl fullWidth variant="filled">
                    <InputLabel id="sort-label">정렬 기준</InputLabel>
                    <Select
                      labelId="sort-label"
                      value={filters.sort}
                      onChange={handleFilterChange}
                      name="sort"
                    >
                      <MenuItem value="companyName">회사명</MenuItem>
                      <MenuItem value="location">위치</MenuItem>
                      <MenuItem value="deadLine">마감일</MenuItem>
                    </Select>
                  </FormControl>
                </Grid>
                {/* Salary Type 필터 */}
                <Grid item xs={6} sm={4}>
                  <FormControl fullWidth variant="filled">
                    <InputLabel id="salaryType-label">급여 유형</InputLabel>
                    <Select
                      labelId="salaryType-label"
                      value={filters.salaryType}
                      onChange={handleFilterChange}
                      name="salaryType"
                    >
                      <MenuItem value="TIME">시급</MenuItem>
                      <MenuItem value="WEEK">주급</MenuItem>
                      <MenuItem value="MONTH">월급</MenuItem>
                      <MenuItem value="YEAR">연봉</MenuItem>
                    </Select>
                  </FormControl>
                </Grid>

                {/* 필터 적용 버튼 */}
                <Grid item xs={12}>
                  <Box sx={{ mt: 2 }}>
                    <Button fullWidth variant="contained" color="primary" onClick={handleSearch}>
                      필터 적용
                    </Button>
                  </Box>
                </Grid>
              </Grid>
            </AccordionDetails>
          </Accordion>


        </Box>
      )}
      <Container>
        <CardList jobOffers={jobOffers} />
        <div ref={lastJobOfferElementRef} />
        {loading && <CircularProgress />}
      </Container>
    </div>
  );
}

export default Home;
