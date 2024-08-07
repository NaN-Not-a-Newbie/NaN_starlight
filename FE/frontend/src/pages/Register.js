import React, { useState } from 'react';
import {
  TextField, Button, Container, Typography, Box, Grid, Stepper, Step, StepLabel, Paper,
  IconButton
} from '@mui/material';
import PhotoCamera from '@mui/icons-material/PhotoCamera';
import axios from 'axios';

const steps = ['Account Info', 'Personal Info', 'ID Verification'];

function Register() {
  const [role, setRole] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [resume, setResume] = useState('');
  const [name, setName] = useState('');
  const [location, setLocation] = useState('');
  const [gender, setGender] = useState('');
  const [age, setAge] = useState('');
  const [idCard, setIdCard] = useState(null);
  const [activeStep, setActiveStep] = useState(0);

  const handleNext = () => setActiveStep(prevStep => prevStep + 1);
  const handleBack = () => setActiveStep(prevStep => prevStep - 1);
  const handleRoleBack = () => setRole('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await axios.post('https://yourapi.com/register', {
        email, password, role, resume, name, location, gender, age, idCard
      });
      localStorage.setItem('token', response.data.token); // JWT 토큰 저장
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
          variant={role === 'employer' ? 'contained' : 'outlined'}
          color="primary"
          onClick={() => setRole('employer')}
          sx={{
            height: '100%',
            transition: 'flex-grow 0.5s',
            flexGrow: role === 'employer' ? 1 : 0,
          }}
        >
          I am an Employer.
        </Button>
      </Grid>
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
          I am a Job Seeker
        </Button>
      </Grid>
    </Grid>
  );

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
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              autoFocus
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="resume"
              label="Resume"
              type="text"
              id="resume"
              autoComplete="resume"
              value={resume}
              onChange={(e) => setResume(e.target.value)}
            />
            <Button
              fullWidth
              variant="contained"
              color="primary"
              sx={{ mt: 3, mb: 2 }}
              onClick={handleNext}
            >
              Next
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
              label="Name"
              type="text"
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="location"
              label="Location"
              type="text"
              id="location"
              value={location}
              onChange={(e) => setLocation(e.target.value)}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="gender"
              label="Gender"
              type="text"
              id="gender"
              value={gender}
              onChange={(e) => setGender(e.target.value)}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="age"
              label="Age"
              type="number"
              id="age"
              value={age}
              onChange={(e) => setAge(e.target.value)}
            />
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2 }}>
              <Button variant="contained" onClick={handleBack}>
                Back
              </Button>
              <Button variant="contained" color="primary" onClick={handleNext}>
                Next
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
              Upload ID Card
              <input
                type="file"
                hidden
                onChange={(e) => setIdCard(e.target.files[0])}
              />
            </Button>
            <IconButton
              color="primary"
              aria-label="upload picture"
              component="label"
            >
              <input hidden accept="image/*" type="file" onChange={(e) => setIdCard(e.target.files[0])} />
              <PhotoCamera />
            </IconButton>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2 }}>
              <Button variant="contained" onClick={handleBack}>
                Back
              </Button>
              <Button variant="contained" color="primary" onClick={handleSubmit}>
                Register
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
          Register
        </Typography>
        {role ? (
          <>
            <Button onClick={handleRoleBack} sx={{ mb: 2 }}>
              Choose Role Again
            </Button>
            {role === 'employer' ? (
              <Paper elevation={0} sx={{ p: 2, width: '100%' }}>
                <Stepper activeStep={activeStep} alternativeLabel>
                  {steps.map((label) => (
                    <Step key={label}>
                      <StepLabel>{label}</StepLabel>
                    </Step>
                  ))}
                </Stepper>
                {renderEmployerForm()}
              </Paper>
            ) : (
              <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1 }}>
                <TextField
                  variant="outlined"
                  margin="normal"
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  autoComplete="email"
                  autoFocus
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
                <TextField
                  variant="outlined"
                  margin="normal"
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type="password"
                  id="password"
                  autoComplete="current-password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
                <TextField
                  variant="outlined"
                  margin="normal"
                  required
                  fullWidth
                  name="resume"
                  label="Resume"
                  type="text"
                  id="resume"
                  autoComplete="resume"
                  value={resume}
                  onChange={(e) => setResume(e.target.value)}
                />
                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  color="primary"
                  sx={{ mt: 3, mb: 2 }}
                >
                  Register as Job Seeker
                </Button>
              </Box>
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
