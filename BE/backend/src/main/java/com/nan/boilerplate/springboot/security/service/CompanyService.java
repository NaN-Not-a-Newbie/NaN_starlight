package com.nan.boilerplate.springboot.security.service;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.security.dto.*;

public interface CompanyService {
    Company findByUsername(String username);

//    Company findByCompanyId(Long id);

    Company activateCompany(String username);

    Company deActivateCompany(String username);

    RegistrationResponse registration(CompanyRegistrationRequest companyRegistrationRequest);

    AuthenticatedCompanyDto findAuthenticatedCompanyByCompanyName(String username);
}
