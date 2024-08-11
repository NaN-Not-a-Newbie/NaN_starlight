import { Box, Container,Paper,  } from '@mui/material';
import React from 'react';


function Home() {
  return (
    <div>
      <Container>
        <Box
          sx={{
            display: 'flex',
            flexWrap: 'wrap',
            '& > :not(style)': {
              m: 1,
              width: '100%',
              height: 120,
              borderRadius: '10px',
            },
          }}
        >
          <Paper elevation={3} />
          <Paper elevation={3} />
          <Paper elevation={3} />
          <Paper elevation={3} />
          <Paper elevation={3} />
          <Paper elevation={3} />
          <Paper elevation={3} />
          <Paper elevation={3} />
        </Box>
      </Container>
    </div>
  );
}

export default Home;
