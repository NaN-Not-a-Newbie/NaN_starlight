import React, { useRef, useState } from 'react';
import {
  TextField, Button, Container, Typography, Box, Grid, Stepper, Step, StepLabel, Paper,
  IconButton, Select, MenuItem, FormControl, InputLabel,
  CircularProgress
} from '@mui/material';
import { styled } from '@mui/material/styles';
import StepConnector, { stepConnectorClasses } from '@mui/material/StepConnector';
import Check from '@mui/icons-material/Check';
import { StepIconProps } from '@mui/material/StepIcon';
import PhotoCamera from '@mui/icons-material/PhotoCamera';
import SignatureCanvas from 'react-signature-canvas';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const educationMapping = {
  ELEM: '초졸',
  MIDDLE: '중졸',
  HIGH: '고졸',
  UNIV: '대졸',
  MASTER: '석사',
  DOCTOR: '박사'
};
const steps = ['계정 정보', '개인 정보', '세부 정보', '사인 등록'];
const steps_E = ['계정 정보', '회사 정보']
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
    password2: '',
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
  const [loading, setLoading] = useState(false);
  const [employerData, setEmployerData] = useState({
    companyName: '',
    username: '',
    password: '',
    password2: '',
    phoneNum: '',
  });

  const [activeStep, setActiveStep] = useState(0);
  const [passwordError, setPasswordError] = useState('');

  const fileInputRef = useRef(null);

  const navigate = useNavigate();
  const handleFileChange = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);
    setLoading(true);
    try {
      const response = await axios.post('http://localhost:8080/register/company/idCard', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      const [companyAddress, companyRegistrationNumber, companyName] = response.data;
      setEmployerData({
        ...employerData,
        companyName,
        companyRegistrationNumber,
        companyAddress,
      });

    } catch (error) {
      console.error('파일 업로드 중 오류가 발생했습니다.', error);
    } finally {
      setLoading(false);  // 업로드 종료 시 로딩 상태 비활성화
    }
  };

  const handleButtonClick = () => {
    fileInputRef.current.click();  // 파일 선택 다이얼로그 열기
  };
  const handleNext = () => {
    if (activeStep === 0) {
      const passwordValid = validatePassword(jobSeekerData.password);
      if (!passwordValid) {
        setPasswordError('비밀번호는 8자 이상 영문 대소문자, 숫자, 특수문자를 포함해야 합니다.');
        return;
      }
    }
    setPasswordError('');
    setActiveStep((prevStep) => prevStep + 1);
  };
  const handleNext_E = () => {
    if (activeStep === 0) {
      const passwordValid = validatePassword(employerData.password);
      if (!passwordValid) {
        setPasswordError('비밀번호는 8자 이상 영문 대소문자, 숫자, 특수문자를 포함해야 합니다.');
        return;
      }
    }
    setPasswordError('');
    setActiveStep((prevStep) => prevStep + 1);
  };
  const validatePassword = (password) => {
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{8,}$/;
    return regex.test(password);
  };
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

  const sigCanvas = useRef(null);
  const [errorMessage, setErrorMessage] = useState("");

  const handleJobSeekerSubmit = async (event) => {
    event.preventDefault();

    try {
      // 1단계: 사용자 데이터를 서버에 전송
      const response = await axios.post('http://localhost:8080/register/users', jobSeekerData);
      localStorage.setItem('token', response.data.token);
      console.log(response.data);
      const token = localStorage.getItem('token');
      // 2단계: 사인 패드가 비어있는지 확인
      if (sigCanvas.current.isEmpty()) {
        setErrorMessage("사인을 입력해 주세요.");
        return;
      }

      // 3단계: 사인을 PNG로 변환하여 서버에 전송
      const dataURL = sigCanvas.current.getTrimmedCanvas().toDataURL("image/png");
      const blob = dataURLToBlob(dataURL);

      const formData = new FormData();
      const encodedToken = btoa(token);  // 토큰을 base64로 인코딩
      formData.append("file", blob, "signature.png");
      await axios.post(`http://localhost:8080/register/user/sign/${encodedToken}`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        }
      });
      navigate('/');

    } catch (error) {
      console.error('오류가 발생했습니다.', error);
    }
  };

  const dataURLToBlob = (dataURL) => {
    const byteString = atob(dataURL.split(',')[1]);
    const mimeString = dataURL.split(',')[0].split(':')[1].split(';')[0];
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }
    return new Blob([ab], { type: mimeString });
  };

  const handleEmployerSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/register/company', employerData);
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
              error={!!passwordError}
              helperText={passwordError}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="password2"
              label="비밀번호 확인"
              type="password"
              id="password2"
              autoComplete="current-password"
              value={jobSeekerData.password2}
              onChange={handleJobSeekerChange}
              error={!!passwordError}
              helperText={passwordError}
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
            <FormControl variant="outlined" margin="normal" required fullWidth>
              <InputLabel id="education-label">최종 학력</InputLabel>
              <Select
                labelId="education-label"
                id="education"
                name="education"
                value={jobSeekerData.education}
                onChange={handleJobSeekerChange}
                label="최종 학력"
              >
                {Object.keys(educationMapping).map((key) => (
                  <MenuItem key={key} value={key}>
                    {educationMapping[key]}
                  </MenuItem>
                ))}
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

            <FormControl variant="outlined" margin="normal" required fullWidth>
              <InputLabel id="envEyesight-label">시력</InputLabel>
              <Select
                labelId="envEyesight-label"
                id="envEyesight"
                name="envEyesight"
                value={jobSeekerData.envEyesight}
                onChange={handleJobSeekerChange}
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
                value={jobSeekerData.envBothHands}
                onChange={handleJobSeekerChange}
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
                value={jobSeekerData.envhandWork}
                onChange={handleJobSeekerChange}
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
                value={jobSeekerData.envLiftPower}
                onChange={handleJobSeekerChange}
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
                value={jobSeekerData.envStndWalk}
                onChange={handleJobSeekerChange}
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
                value={jobSeekerData.envLstnTalk}
                onChange={handleJobSeekerChange}
                label="듣기/말하기 능력"
              >
                <MenuItem value="HARD">듣고 말하는 작업 어려움</MenuItem>
                <MenuItem value="NOPROBLEM">듣고 말하기에 어려움 없음</MenuItem>
                <MenuItem value="SIMPLETALLK">간단한 듣고 말하기 가능</MenuItem>
                <MenuItem value="DONTCARE">무관</MenuItem>
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
      case 3:
        return (
          <Box component="form" sx={{ mt: 1 }}>
            <SignatureCanvas
              ref={sigCanvas}
              penColor="black"
              canvasProps={{
                width: 395,
                height: 200,
                className: 'sigCanvas',
                style: { border: '1px solid #000' },
              }}
            />

            {errorMessage && (
              <Typography color="error" variant="body2">
                {errorMessage}
              </Typography>
            )}

            <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2 }}>

              <Button variant="contained" onClick={handleBack}>
                이전
              </Button>
              <Button variant="contained" onClick={() => sigCanvas.current.clear()}>
                초기화
              </Button>
              <Button variant="contained" color="primary" onClick={handleJobSeekerSubmit}>
                제출
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
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="password2"
              label="비밀번호 확인"
              type="password"
              id="password2"
              autoComplete="current-password"
              value={employerData.password2}
              onChange={handleEmployerChange}
            />
            <Button
              fullWidth
              variant="contained"
              color="primary"
              sx={{ mt: 3, mb: 2 }}
              onClick={handleNext_E}
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
              label="사업자 등록번호"
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

            <Button
              variant="contained"
              color="primary"
              fullWidth
              sx={{ mt: 2 }}
              onClick={handleButtonClick}
            >
              사업자 등록증 업로드
            </Button>
            <input
              type="file"
              ref={fileInputRef}
              style={{ display: 'none' }}
              onChange={handleFileChange}
            />

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
                  {steps_E.map((label) => (
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
      {loading && (
        <Box
          sx={{
            position: 'fixed',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            backgroundColor: 'rgba(255, 255, 255, 0.8)',
            zIndex: 9999,
          }}
        >
          <CircularProgress />
        </Box>
      )}
    </Container>
  );
}

export default Register;
