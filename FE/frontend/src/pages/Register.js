import React, { useState } from 'react';
import {
  TextField, Button, Container, Typography, Box, Grid, Stepper, Step, StepLabel, Paper,
  IconButton, Select, MenuItem, FormControl, InputLabel
} from '@mui/material';
import { styled } from '@mui/material/styles';
import StepConnector, { stepConnectorClasses } from '@mui/material/StepConnector';
import Check from '@mui/icons-material/Check';
import { StepIconProps } from '@mui/material/StepIcon';
import PhotoCamera from '@mui/icons-material/PhotoCamera';
import axios from 'axios';

const steps = ['계정 정보', '개인 정보', '세부 정보'];
const QontoConnector = styled(StepConnector)(({ theme }) => ({
  [`&.${stepConnectorClasses.alternativeLabel}`]: {
    top: 10,
    left: 'calc(-50% + 16px)',
    right: 'calc(50% + 16px)',
  },
  [`&.${stepConnectorClasses.active}`]: {
    [`& .${stepConnectorClasses.line}`]: {
      borderColor: '#784af4',
    },
  },
  [`&.${stepConnectorClasses.completed}`]: {
    [`& .${stepConnectorClasses.line}`]: {
      borderColor: '#784af4',
    },
  },
  [`& .${stepConnectorClasses.line}`]: {
    borderColor: theme.palette.mode === 'dark' ? theme.palette.grey[800] : '#eaeaf0',
    borderTopWidth: 3,
    borderRadius: 1,
  },
}));
const QontoStepIconRoot = styled('div')(({ theme, ownerState }) => ({
  color: theme.palette.mode === 'dark' ? theme.palette.grey[700] : '#eaeaf0',
  display: 'flex',
  height: 22,
  alignItems: 'center',
  ...(ownerState.active && {
    color: '#784af4',
  }),
  '& .QontoStepIcon-completedIcon': {
    color: '#784af4',
    zIndex: 1,
    fontSize: 18,
  },
  '& .QontoStepIcon-circle': {
    width: 8,
    height: 8,
    borderRadius: '50%',
    backgroundColor: 'currentColor',
  },
}));

function QontoStepIcon(props) {
  const { active, completed, className } = props;

  return (
    <QontoStepIconRoot ownerState={{ active }} className={className}>
      {completed ? (
        <Check className="QontoStepIcon-completedIcon" />
      ) : (
        <div className="QontoStepIcon-circle" />
      )}
    </QontoStepIconRoot>
  );
}

function Register() {
  const [role, setRole] = useState('');
  const [jobSeekerData, setJobSeekerData] = useState({
    name: '',
    username: '',
    password: '',
    birthday: '',
    envEyesight: '',
    envBothHands: '',
    envhandWork: '',
    envLiftPower: '',
    envStndWalk: '',
    envLstnTalk: '',
    education: '',
    male: null,
    phoneNum: ''
  });

  const [employerData, setEmployerData] = useState({
    companyRegistrationNumber: 0,
    companyName: '',
    username: '',
    password: '',
    password2: '',
    phoneNum: '',
    companyAddress: ''
  });

  const [activeStep, setActiveStep] = useState(0);

  const handleNext = () => setActiveStep(prevStep => prevStep + 1);
  const handleBack = () => setActiveStep(prevStep => prevStep - 1);
  const handleRoleBack = () => setRole('');

  const handleJobSeekerChange = (e) => {
    const { name, value } = e.target;
    setJobSeekerData({ ...jobSeekerData, [name]: value });
  };

  const handleEmployerChange = (e) => {
    const { name, value } = e.target;
    setEmployerData({ ...employerData, [name]: value });
  };

  const handleJobSeekerSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await axios.post('/register/user', jobSeekerData);
      localStorage.setItem('token', response.data.token);
      console.log(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  const handleEmployerSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await axios.post('/register/company', employerData);
      localStorage.setItem('token', response.data.token);
      console.log(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  const renderRoleButtons = () => (
    <Grid container spacing={2} sx={{ mt: 3 }}>
      <Grid item xs={12} sm={role ? 12 : 6}>
        <Button
          fullWidth
          variant={role === 'jobseeker' ? 'contained' : 'outlined'}
          color="primary"
          onClick={() => setRole('jobseeker')}
          sx={{
            height: '100%',
            transition: 'flex-grow 0.5s',
            flexGrow: role === 'jobseeker' ? 1 : 0,
          }}
        >
          일을 찾고 있어요.
        </Button>
      </Grid>
      <Grid item xs={12} sm={role ? 12 : 6}>
        <Button
          fullWidth
          variant={role === 'employer' ? 'contained' : 'outlined'}
          color="primary"
          onClick={() => setRole('employer')}
          sx={{
            height: '100%',
            transition: 'flex-grow 0.5s',
            flexGrow: role === 'employer' ? 1 : 0,
          }}
        >
          일할 사람을 찾고 있어요.
        </Button>
      </Grid>
    </Grid>
  );

  const renderJobSeekerForm = () => {
    switch (activeStep) {
      case 0:
        return (
          <Box component="form" sx={{ mt: 1 }}>
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
              value={jobSeekerData.username}
              onChange={handleJobSeekerChange}
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
              value={jobSeekerData.password}
              onChange={handleJobSeekerChange}
            />
            <Button
              fullWidth
              variant="contained"
              color="primary"
              sx={{ mt: 3, mb: 2 }}
              onClick={handleNext}
            >
              다음
            </Button>
          </Box>
        );
      case 1:
        return (
          <Box component="form" sx={{ mt: 1 }}>
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="name"
              label="성함"
              type="text"
              id="name"
              value={jobSeekerData.name}
              onChange={handleJobSeekerChange}
            />
            <span>생년월일</span>
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="birthday"
              type="date"
              id="birthday"
              value={jobSeekerData.birthday}
              onChange={handleJobSeekerChange}
            />

            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="phoneNum"
              label="전화번호"
              type="text"
              id="phoneNum"
              value={jobSeekerData.phoneNum}
              onChange={handleJobSeekerChange}
            />
            <FormControl variant="outlined" margin="normal" required fullWidth>
              <InputLabel id="male-label">성별</InputLabel>
              <Select
                labelId="male-label"
                id="male"
                name="male"
                value={jobSeekerData.male !== null ? (jobSeekerData.male ? '남성' : '여성') : ''}
                onChange={(e) =>
                  handleJobSeekerChange({
                    target: { name: 'male', value: e.target.value === '남성' },
                  })
                }
                label="성별"
              >
                <MenuItem value="남성">남성</MenuItem>
                <MenuItem value="여성">여성</MenuItem>
              </Select>
            </FormControl>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2 }}>
              <Button variant="contained" onClick={handleBack}>
                이전
              </Button>
              <Button variant="contained" color="primary" onClick={handleNext}>
                다음
              </Button>
            </Box>
          </Box>
        );
      case 2:
        return (
          <Box component="form" sx={{ mt: 1 }}>

            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="envEyesight"
              label="시력"
              type="text"
              id="envEyesight"
              value={jobSeekerData.envEyesight}
              onChange={handleJobSeekerChange}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="envBothHands"
              label="양손 사용 능력"
              type="text"
              id="envBothHands"
              value={jobSeekerData.envBothHands}
              onChange={handleJobSeekerChange}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="envhandWork"
              label="손작업 정확도"
              type="text"
              id="envhandWork"
              value={jobSeekerData.envhandWork}
              onChange={handleJobSeekerChange}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="envLiftPower"
              label="들기 힘 (kg)"
              type="text"
              id="envLiftPower"
              value={jobSeekerData.envLiftPower}
              onChange={handleJobSeekerChange}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="envStndWalk"
              label="서있기/걷기 능력"
              type="text"
              id="envStndWalk"
              value={jobSeekerData.envStndWalk}
              onChange={handleJobSeekerChange}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="envLstnTalk"
              label="듣기/말하기 능력"
              type="text"
              id="envLstnTalk"
              value={jobSeekerData.envLstnTalk}
              onChange={handleJobSeekerChange}
            />
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2 }}>
              <Button variant="contained" onClick={handleBack}>
                이전
              </Button>
              <Button variant="contained" color="primary" onClick={handleJobSeekerSubmit}>
                등록
              </Button>
            </Box>
          </Box>
        );
      default:
        return null;
    }
  };

  const renderEmployerForm = () => {
    switch (activeStep) {
      case 0:
        return (
          <Box component="form" sx={{ mt: 1 }}>
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
              value={employerData.username}
              onChange={handleEmployerChange}
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
              value={employerData.password}
              onChange={handleEmployerChange}
            />
            <Button
              fullWidth
              variant="contained"
              color="primary"
              sx={{ mt: 3, mb: 2 }}
              onClick={handleNext}
            >
              다음
            </Button>
          </Box>
        );
      case 1:
        return (
          <Box component="form" sx={{ mt: 1 }}>
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="companyName"
              label="회사명"
              type="text"
              id="companyName"
              value={employerData.companyName}
              onChange={handleEmployerChange}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="companyRegistrationNumber"
              label="회사 등록번호"
              type="number"
              id="companyRegistrationNumber"
              value={employerData.companyRegistrationNumber}
              onChange={handleEmployerChange}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="phoneNum"
              label="전화번호"
              type="text"
              id="phoneNum"
              value={employerData.phoneNum}
              onChange={handleEmployerChange}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="companyAddress"
              label="회사 주소"
              type="text"
              id="companyAddress"
              value={employerData.companyAddress}
              onChange={handleEmployerChange}
            />
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2 }}>
              <Button variant="contained" onClick={handleBack}>
                이전
              </Button>
              <Button variant="contained" color="primary" onClick={handleNext}>
                다음
              </Button>
            </Box>
          </Box>
        );
      case 2:
        return (
          <Box component="form" sx={{ mt: 1 }}>
            <Button
              variant="contained"
              component="label"
              fullWidth
              sx={{ mt: 1, mb: 1 }}
            >
              신분증 업로드
              <input
                type="file"
                hidden
                onChange={(e) => setEmployerData({ ...employerData, idCard: e.target.files[0] })}
              />
            </Button>
            <IconButton
              color="primary"
              aria-label="사진 업로드"
              component="label"
            >
              <input hidden accept="image/*" type="file" onChange={(e) => setEmployerData({ ...employerData, idCard: e.target.files[0] })} />
              <PhotoCamera />
            </IconButton>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2 }}>
              <Button variant="contained" onClick={handleBack}>
                이전
              </Button>
              <Button variant="contained" color="primary" onClick={handleEmployerSubmit}>
                등록
              </Button>
            </Box>
          </Box>
        );
      default:
        return null;
    }
  };

  return (
    <Container component="main" maxWidth="md">
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Typography component="h1" variant="h5">
          <b>회원가입</b>
        </Typography>
        {role ? (
          <>
            <Button onClick={handleRoleBack} sx={{ mb: 2 }}>
              역할 다시 선택
            </Button>
            {role === 'employer' ? (
              <Paper elevation={0} sx={{ p: 2, width: '100%' }}>
                <Stepper activeStep={activeStep} alternativeLabel connector={<QontoConnector />}>
                  {steps.map((label) => (
                    <Step key={label}>
                      <StepLabel StepIconComponent={QontoStepIcon}>{label}</StepLabel>
                    </Step>
                  ))}
                </Stepper>

                {renderEmployerForm()}
              </Paper>
            ) : (
              <Paper elevation={0} sx={{ p: 2, width: '100%' }}>
                <Stepper activeStep={activeStep} alternativeLabel connector={<QontoConnector />}>
                  {steps.map((label) => (
                    <Step key={label}>
                      <StepLabel StepIconComponent={QontoStepIcon}>{label}</StepLabel>
                    </Step>
                  ))}
                </Stepper>

                {renderJobSeekerForm()}
              </Paper>
            )}
          </>
        ) : (
          renderRoleButtons()
        )}
      </Box>
    </Container>
  );
}

export default Register;
