import React from 'react';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import { Link } from 'react-router-dom';

function MyAppBar() {
  return (
    <AppBar position="static">
      <Toolbar>
      <Typography 
          variant="h6" 
          component="div" 
          sx={{ flexGrow: 1, fontFamily: 'Danjo-bold-Regular, Arial, sans-serif' }}
        >
          별자리✨
        </Typography>
        <Button color="inherit" component={Link} to="/login">로그인</Button>
        <Button color="inherit" component={Link} to="/register">가입</Button>
      </Toolbar>
    </AppBar>
  );
}

export default MyAppBar;
