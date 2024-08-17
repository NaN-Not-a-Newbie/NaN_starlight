import { Paper, Typography, Box, Stack } from '@mui/material';
import React from 'react';
import dayjs from 'dayjs';
import { useNavigate } from 'react-router-dom';

const calculateDDay = (deadline) => {
    if (!deadline) return '상시 모집';
    const today = dayjs().startOf('day');
    const endDate = dayjs(deadline).startOf('day');
    const diff = endDate.diff(today, 'day');

    if (diff > 0) {
        return `D-${diff}`;
    } else if (diff === 0) {
        return 'D-Day';
    } else {
        return '모집 완료';
    }
};

const educationMapping = {
    DONTCARE: '학력 무관',
    ELEM: '초졸',
    MIDDLE: '중졸',
    HIGH: '고졸',
    UNIV: '대졸',
    MASTER: '석사',
    DOCTOR: '박사'
};

const salaryTypeMapping = {
    TIME: '시급',
    WEEK: '주급',
    MONTH: '월급',
    YEAR: '연봉'
};

const formatSalary = (salary) => {
    return salary.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
};

const truncateTitle = (title, maxLength) => {
    if (title.length > maxLength) {
        return title.substring(0, maxLength) + '...';
    }
    return title;
};

const CardList = ({ jobOffers }) => {
    const navigate = useNavigate();

    const handleCardClick = (id) => {
        navigate(`/job/${id}`);
    };

    return (
        <Box
            sx={{
                display: 'flex',
                flexWrap: 'wrap',
                '& > :not(style)': {
                    m: 1,
                    width: '100%',
                    height: 120,
                    borderRadius: '10px',
                    cursor: 'pointer',
                },
            }}
        >
            {jobOffers.map((offer) => {
                const dDayText = calculateDDay(offer.deadLine);
                const isComplete = dDayText === '모집 완료';
                const isDDay = dDayText === 'D-Day';

                return (
                    <Paper
                        key={offer.id}
                        elevation={3}
                        sx={{ 
                            padding: '16px',
                        }}
                        onClick={() => handleCardClick(offer.id)}
                    >
                        <Typography
                            variant="h6"
                            sx={{
                                textDecoration: isComplete ? 'line-through' : 'none',
                                fontStyle: isComplete ? 'italic' : 'normal',
                                color: isComplete ? 'gray' : 'inherit',
                            }}
                        >
                            <b style={{ color: isDDay ? 'red' : 'inherit' }}>
                                [{dDayText}]
                            </b> {truncateTitle(offer.title, 15)}
                        </Typography>
                        <Typography variant="body1">
                            {salaryTypeMapping[offer.salaryType]} {formatSalary(offer.salary)}원
                        </Typography>
                        <Stack direction={'row'} spacing={2}>
                            <Typography variant="body1">경력:  {offer.career? offer.career+"년 이상" : "신입"}</Typography>
                        </Stack>
                        <Stack direction={'row'} spacing={2}>
                            <Typography variant="body1">학력: {educationMapping[offer.education]}</Typography>
                        </Stack>
                    </Paper>
                );
            })}
        </Box>
    );
};

export default CardList;
