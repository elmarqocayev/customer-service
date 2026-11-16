package com.example.customerservice.dto;

import com.example.customerservice.domain.Customer;

import java.time.Instant;
import java.util.List;

public record CustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        List<AddressDto> addresses,
        Instant createdAt) {

    public static CustomerResponse from(Customer c) {
        List<AddressDto> addresses = c.getAddresses().stream()
                .map(a -> new AddressDto(a.getStreet(), a.getCity(), a.getCountry()))
                .toList();
        return new CustomerResponse(c.getId(), c.getFirstName(), c.getLastName(),
                c.getEmail(), c.getPhone(), addresses, c.getCreatedAt());
    }
}
