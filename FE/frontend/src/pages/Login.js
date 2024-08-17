import React, { useState } from 'react';
import { TextField, Button, Container, Typography, Box, FormControlLabel, Switch, Stack } from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';

function Login() {
  const [username, setUsername] = useState('');  // 변수명 수정: setEmail -> setUsername
  const [password, setPassword] = useState('');
  const [isCompany, setIsCompany] = useState(false);  // 회사인지 여부를 저장하는 상태
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    const loginUrl = isCompany ? 'http://localhost:8080/login/company' : 'http://localhost:8080/login/user';

    try {
      const response = await axios.post(loginUrl, { username, password });
      localStorage.setItem('token', response.data.token); // JWT 토큰 저장

      if (isCompany) {
        sessionStorage.setItem('company', 'true');  // 회사일 경우 세션 스토리지에 저장
      } else {
        sessionStorage.removeItem('company');  // 개인 사용자일 경우 세션 스토리지에서 제거
      }

      navigate('/');
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <Container component="main" maxWidth="xs">
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Typography component="h1" variant="h5">
          <b>로그인</b>
        </Typography>


        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1 }}>
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            id="username"
            label="아이디"
            name="username"
            autoComplete="username"
            autoFocus
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            name="password"
            label="비밀번호"
            type="password"
            id="password"
            autoComplete="current-password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            sx={{ mt: 3, mb: 2 }}
          >
            로그인
          </Button>
          <Stack direction={'row'} justifyContent={'space-between'}>
            <FormControlLabel
              control={<Switch checked={isCompany} onChange={() => setIsCompany(!isCompany)} />}
              label="회사 계정으로 로그인"
              sx={{ mb: 2 }}
            />

            <Typography variant="body2" color="textSecondary" style={{paddingTop:'10px'}}>
              회원이 아니신가요?{' '}
              <Link to="/register" style={{ textDecoration: 'underline', color: '#1976d2' }}>
                회원가입
              </Link>
            </Typography>
          </Stack>


        </Box>
      </Box>
    </Container>
  );
}

export default Login;
