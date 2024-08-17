// App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ThemeProvider } from '@mui/material/styles';
import { Box } from '@mui/material';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import AppBar from './components/AppBar';
import Footer from './components/Footer';
import { theme, globalStyles } from './theme';
import Notification from './pages/Notification';
import JobDetail from './pages/JobDetail';
import Jobs from './pages/Jobs';
import MyPage from './pages/MyPage';
import WriteOffer from './pages/WriteOffer';

function App() {
  return (
    <ThemeProvider theme={theme}>
      {globalStyles}
      <Router>
        <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
          <AppBar />
          <Box sx={{ flex: 1 }}>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/notification" element={<Notification />} />
              <Route path="/jobs" element={<Jobs />} />
              <Route path="/job/:id" element={<JobDetail />} />
              <Route path="/mypage" element={<MyPage/>} />
              <Route path="/writeoffer" element={<WriteOffer/>}/>
            </Routes>
          </Box>
          <Footer />
        </Box>
      </Router>
    </ThemeProvider>
  );
}

export default App;
