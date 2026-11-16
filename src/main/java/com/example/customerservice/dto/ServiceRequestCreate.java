package com.example.customerservice.dto;

import com.example.customerservice.domain.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record ServiceRequestCreate(
        @NotNull Long customerId,
        @NotBlank String title,
        String description,
        @NotNull Priority priority,
        Set<String> tags) {
}
