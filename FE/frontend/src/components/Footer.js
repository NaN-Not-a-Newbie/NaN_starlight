import React, { useState, useEffect } from 'react';
import { BottomNavigation, BottomNavigationAction, Box } from '@mui/material';
import HomeIcon from '@mui/icons-material/Home';
import WorkIcon from '@mui/icons-material/Work';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function Footer() {
  const [value, setValue] = useState(0);
  const [isCompany, setIsCompany] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    // /me 엔드포인트를 호출하여 role을 확인
    const fetchUserRole = async () => {
      const token = localStorage.getItem('token');
      if (token) {
        try {
          const response = await axios.get('http://localhost:8080/me', {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          setIsCompany(response.data.role === 'COMPANY');
        } catch (error) {
          console.error('Error fetching user role:', error);
        }
      }
    };

    fetchUserRole();
  }, [isCompany]);

  const handleNavigationChange = (event, newValue) => {
    setValue(newValue);

    if (newValue === 0) {
      navigate('/');
    } else if (newValue === 1) {
      if (isCompany) {
        navigate('/writeoffer');  // 공고 작성 페이지로 이동
      } else {
        navigate('/jobs');  // 채용 공고 페이지로 이동
      }
    } else if (newValue === 2) {
      const token = localStorage.getItem('token');
      if (token) {
        navigate('/mypage');
      } else {
        navigate('/login');
      }
    }
  };

  return (
    <>
      <Box
        sx={{
          position: 'fixed',
          bottom: 0,
          width: '100%',
          backgroundColor: 'primary.main',
          zIndex: 1000,
          borderTop: '2px solid #ccc',
        }}
      >
        <BottomNavigation value={value} onChange={handleNavigationChange} showLabels>
          <BottomNavigationAction label="홈" icon={<HomeIcon />} />
          <BottomNavigationAction
            label={isCompany ? "공고 작성" : "채용공고"}
            icon={isCompany ? <AddCircleIcon /> : <WorkIcon />}
          />
          <BottomNavigationAction label="마이 페이지" icon={<AccountCircleIcon />} />
        </BottomNavigation>
      </Box>
      <Box sx={{ paddingBottom: '56px' }} />
    </>
  );
}

export default Footer;
