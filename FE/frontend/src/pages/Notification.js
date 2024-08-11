import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const Notification = () => {
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) {
            // 토큰이 없으면 /login으로 리다이렉트
            navigate('/login');
        }
    }, [navigate]);

    return (
        <div>
            <h2>알림</h2>
        </div>
    );
};

export default Notification;
