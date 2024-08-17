import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom'; // useLocation 추가
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import { Link, useNavigate } from 'react-router-dom';
import NotificationsIcon from '@mui/icons-material/Notifications';
import HomeIcon from '@mui/icons-material/Home';
import CloseIcon from '@mui/icons-material/Close';
import { FilledInput, InputAdornment } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';

function MyAppBar() {
  const [drawerOpen, setDrawerOpen] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const location = useLocation(); // 현재 경로 가져오기
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    setIsLoggedIn(!!token);  // 토큰이 있으면 true, 없으면 false
  }, [isLoggedIn]);

  const toggleDrawer = (open) => (event) => {
    if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
      return;
    }
    setDrawerOpen(open);
  };

  const handleAuthButtonClick = () => {
    if (isLoggedIn) {
      // 로그아웃 처리
      localStorage.removeItem('token');
      sessionStorage.removeItem('company');
      setIsLoggedIn(false);
      navigate('/')
    } else {
      // 로그인 페이지로 이동
      window.location.href = '/login';
    }
  };

  const handleNotificationClick = () => {
    if (!isLoggedIn) {
      navigate('/login'); // 로그인되지 않은 경우 /login으로 리다이렉트
    } else {
      navigate('/notification'); // 로그인된 경우 알림 페이지로 이동
    }
  };

  const sideList = () => (
    <Box
      sx={{ width: '100%', height: '100%', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}
      role="presentation"
      onClick={toggleDrawer(false)}
      onKeyDown={toggleDrawer(false)}
    >
      <Box>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '0 16px', paddingTop: '0.5rem' }}>
          <IconButton component={Link} to="/" color="inherit">
            <HomeIcon />
          </IconButton>
          <IconButton onClick={toggleDrawer(false)} color="inherit">
            <CloseIcon />
          </IconButton>
        </Box>
        <List>
          {/* <ListItem button component={Link} to="/">
            <ListItemText primary="홈" />
          </ListItem>
          <ListItem button component={Link} to="/login">
            <ListItemText primary="로그인" />
          </ListItem>
          <ListItem button component={Link} to="/register">
            <ListItemText primary="회원가입" />
          </ListItem> */}
        </List>
      </Box>
      <Box sx={{ padding: 2 }}>
        <Button
          variant="outlined"
          fullWidth
          onClick={handleAuthButtonClick}
          sx={{ borderColor: 'inherit', color: 'inherit' }}
        >
          {isLoggedIn ? '로그아웃' : '로그인'}
        </Button>
      </Box>
    </Box>
  );

  return (
    <>
      <AppBar position="static">
        <Toolbar>
          <Typography
            variant="h6"
            component={Link}
            to="/"
            sx={{ flexGrow: 1, fontFamily: 'Danjo-bold-Regular, Arial, sans-serif', cursor: 'pointer', textDecoration: 'none', color: 'inherit' }}
          >
            별자리✨
          </Typography>
          <IconButton color="inherit" onClick={handleNotificationClick}>
            <NotificationsIcon />
          </IconButton>
          <IconButton
            edge="end"
            color="inherit"
            onClick={toggleDrawer(true)}
          >
            <MenuIcon />
          </IconButton>
        </Toolbar>
      </AppBar>

      

      <Drawer
        anchor="right"
        open={drawerOpen}
        onClose={toggleDrawer(false)}
        PaperProps={{
          sx: { width: '100%' }
        }}
      >
        {sideList()}
      </Drawer>
    </>
  );
}

export default MyAppBar;
