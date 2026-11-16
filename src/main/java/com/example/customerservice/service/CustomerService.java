package com.example.customerservice.service;

import com.example.customerservice.dto.CustomerRequest;
import com.example.customerservice.dto.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    CustomerResponse create(CustomerRequest request);
    CustomerResponse get(Long id);
    Page<CustomerResponse> list(Pageable pageable);
    CustomerResponse update(Long id, CustomerRequest request);
    void delete(Long id);
}
