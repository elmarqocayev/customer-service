package com.example.customerservice.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressDto(
        @NotBlank String street,
        @NotBlank String city,
        @NotBlank String country) {
}
