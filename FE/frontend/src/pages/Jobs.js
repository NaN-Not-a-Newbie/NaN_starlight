import React, { useEffect, useState, useRef } from 'react';
import { Container, CircularProgress, FormControl, InputLabel, Select, MenuItem, Box } from '@mui/material';
import axios from 'axios';
import CardList from '../components/CardList';
import { useNavigate } from 'react-router-dom';

function Jobs() {
  const [jobOffers, setJobOffers] = useState([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const [sortBy, setSortBy] = useState('deadLine');  // 기본 정렬 기준을 deadLine으로 설정
  const observer = useRef();
  const navigate = useNavigate();  

  useEffect(() => {
    const fetchJobOffers = async () => {
      setLoading(true);
      const token = localStorage.getItem('token'); // 저장된 토큰 가져오기
      try {
        const response = await axios.get(`http://localhost:8080/jobOffer/initial?page=${page}&size=10&sort=${sortBy}`, {
          headers: {
            Authorization: `Bearer ${token}` // JWT 토큰을 헤더에 추가
          }
        });
        setJobOffers(prevOffers => [...prevOffers, ...response.data.content]);
        setHasMore(response.data.content.length > 0);
      } catch (error) {
        if (error.response && error.response.status === 401) {
          // 401 에러 발생 시 로그인 페이지로 리다이렉트
          navigate('/login');
        } else {
          console.error('오류가 발생했습니다.', error);
        }
      }
      setLoading(false);
    };

    fetchJobOffers();
  }, [page, sortBy, navigate]);  // sortBy를 종속성 배열에 추가하여 정렬 기준이 변경될 때마다 요청

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

  const handleSortChange = (event) => {
    setSortBy(event.target.value);
    setPage(0);  // 페이지를 초기화하고 새로운 정렬 기준으로 데이터를 다시 불러옵니다.
    setJobOffers([]);  // 기존 데이터를 초기화하여 새 데이터를 불러오게 합니다.
  };

  return (
    <div>
      <Container>
        {/* 정렬 기준 선택 필터 */}
        {/* <Box sx={{ marginBottom: 2 }}>
          <FormControl fullWidth>
            <Select
              labelId="sort-by-label"
              id="sort-by"
              value={sortBy}
              label="필터"
              onChange={handleSortChange}
            >
              <MenuItem value="id">등록순</MenuItem>
              <MenuItem value="title">제목</MenuItem>
              <MenuItem value="deadLine">마감일</MenuItem>
            </Select>
          </FormControl>
        </Box> */}
        
        <CardList jobOffers={jobOffers} />
        <div ref={lastJobOfferElementRef} />
        {loading && <CircularProgress />}
      </Container>
    </div>
  );
}

export default Jobs;
