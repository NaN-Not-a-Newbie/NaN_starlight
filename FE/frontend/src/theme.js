// theme.js
import { createTheme } from '@mui/material/styles';
import { GlobalStyles } from '@mui/material';

const theme = createTheme({
  palette: {
    primary: {
      main: '#2E253C', // 원하는 색상 코드로 변경
    },
  },
  typography: {
    fontFamily: '"Noto Sans KR", sans-serif',
  },
});

const globalStyles = (
  <GlobalStyles
    styles={{
      '@font-face': {
        fontFamily: 'Danjo-bold-Regular',
        src: 'url(https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_2307-1@1.1/Danjo-bold-Regular.woff2) format("woff2")',
        fontWeight: 'normal',
        fontStyle: 'normal',
      },
      body: {
        fontFamily: '"Noto Sans KR", sans-serif',
      },
    }}
  />
);

export { theme, globalStyles };
