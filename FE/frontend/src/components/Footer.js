// components/Footer.js
import React from 'react';
import { Box, Typography } from '@mui/material';

function Footer() {
  return (
    <Box 
      sx={{ 
        position: 'fixed', 
        bottom: 0, 
        width: '100%', 
        p: 2, 
        textAlign: 'center', 
        backgroundColor: 'primary.main', 
        color: 'white' 
      }}
    >
      <Typography variant="body2">
        © 2024 Your Company. All rights reserved.
      </Typography>
    </Box>
  );
}

export default Footer;
