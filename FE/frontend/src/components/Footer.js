import React, { useState } from 'react';
import { BottomNavigation, BottomNavigationAction, Box } from '@mui/material';
import HomeIcon from '@mui/icons-material/Home';
import WorkIcon from '@mui/icons-material/Work';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import { useNavigate } from 'react-router-dom';

function Footer() {
  const [value, setValue] = useState(0);
  const navigate = useNavigate();

  const handleNavigationChange = (event, newValue) => {
    setValue(newValue);

    // 각 네비게이션 아이템 클릭 시 해당 경로로 이동
    if (newValue === 0) {
      navigate('/');
    } else if (newValue === 1) {
      navigate('/jobs');
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
          <BottomNavigationAction label="채용공고" icon={<WorkIcon />} />
          <BottomNavigationAction label="마이 페이지" icon={<AccountCircleIcon />} />
        </BottomNavigation>
      </Box>
      <Box sx={{ paddingBottom: '56px' }} />
    </>
  );
}

export default Footer;
