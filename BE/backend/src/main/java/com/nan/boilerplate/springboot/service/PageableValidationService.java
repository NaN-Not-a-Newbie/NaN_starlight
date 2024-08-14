package com.nan.boilerplate.springboot.service;

import com.nan.boilerplate.springboot.exceptions.BadRequestException;
import com.nan.boilerplate.springboot.model.JobOffer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PageableValidationService {
    private final Class<JobOffer> entityClass = JobOffer.class;


    public Pageable validateAndCorrectPageable(Pageable pageable) {
        List<Sort.Order> validOrders = pageable.getSort().stream()
                .filter(order -> isValidField(order.getProperty()))
                .collect(Collectors.toList());

        if (validOrders.isEmpty()) {
            throw new BadRequestException("존재하지 않는 컬럼 입니다.");
        } else {
            return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(validOrders));
        }
    }

    private boolean isValidField(String fieldName) {
        try {
            Field field = entityClass.getDeclaredField(fieldName);
            return field != null;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
}
