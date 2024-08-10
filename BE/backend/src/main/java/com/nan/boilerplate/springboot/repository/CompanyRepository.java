package com.nan.boilerplate.springboot.repository;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByUsername(String username);

//    Company findByCompanyId(Long id);

    boolean existsByUsername(String username);
}
