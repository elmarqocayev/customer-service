package com.example.customerservice.dto;

import com.example.customerservice.domain.RequestStatus;
import jakarta.validation.constraints.NotNull;

public record StatusChangeRequest(@NotNull RequestStatus status) {
}
