package com.example.customerservice.service;

import com.example.customerservice.domain.Address;
import com.example.customerservice.domain.Customer;
import com.example.customerservice.dto.AddressDto;
import com.example.customerservice.dto.CustomerRequest;
import com.example.customerservice.dto.CustomerResponse;
import com.example.customerservice.exception.BusinessRuleException;
import com.example.customerservice.exception.DuplicateResourceException;
import com.example.customerservice.exception.ResourceNotFoundException;
import com.example.customerservice.repository.CustomerRepository;
import com.example.customerservice.repository.ServiceRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ServiceRequestRepository serviceRequestRepository;

    @Override
    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        if (customerRepository.existsByEmailIgnoreCase(request.email())) {
            throw new DuplicateResourceException("Bu email artıq mövcuddur: " + request.email());
        }
        Customer customer = new Customer();
        apply(customer, request);
        return CustomerResponse.from(customerRepository.save(customer));
    }

    @Override
    public CustomerResponse get(Long id) {
        return CustomerResponse.from(find(id));
    }

    @Override
    public Page<CustomerResponse> list(Pageable pageable) {
        return customerRepository.findAll(pageable).map(CustomerResponse::from);
    }

    @Override
    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {
        Customer customer = find(id);
        if (!customer.getEmail().equalsIgnoreCase(request.email())
                && customerRepository.existsByEmailIgnoreCase(request.email())) {
            throw new DuplicateResourceException("Bu email artıq mövcuddur: " + request.email());
        }
        apply(customer, request);
        return CustomerResponse.from(customer); // dirty checking özü save edir
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Customer customer = find(id);
        if (serviceRequestRepository.existsByCustomerId(id)) {
            throw new BusinessRuleException("Müraciəti olan müştərini silmək olmaz");
        }
        customerRepository.delete(customer);
    }

    private Customer find(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Müştəri", id));
    }

    private void apply(Customer customer, CustomerRequest request) {
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());
        customer.getAddresses().clear();
        if (request.addresses() != null) {
            for (AddressDto dto : request.addresses()) {
                Address address = new Address();
                address.setStreet(dto.street());
                address.setCity(dto.city());
                address.setCountry(dto.country());
                customer.addAddress(address);
            }
        }
    }
}
