package com.example.customerservice.controller;

import com.example.customerservice.domain.RequestStatus;
import com.example.customerservice.dto.ServiceRequestCreate;
import com.example.customerservice.dto.ServiceRequestResponse;
import com.example.customerservice.dto.StatusChangeRequest;
import com.example.customerservice.service.ServiceRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
public class ServiceRequestController {

    private final ServiceRequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceRequestResponse create(@Valid @RequestBody ServiceRequestCreate request) {
        return requestService.create(request);
    }

    @GetMapping("/{id}")
    public ServiceRequestResponse get(@PathVariable Long id) {
        return requestService.get(id);
    }

    @GetMapping
    public Page<ServiceRequestResponse> list(@RequestParam(required = false) RequestStatus status,
                                             Pageable pageable) {
        return requestService.list(status, pageable);
    }

    @PatchMapping("/{id}/status")
    public ServiceRequestResponse changeStatus(@PathVariable Long id,
                                               @Valid @RequestBody StatusChangeRequest body) {
        return requestService.changeStatus(id, body.status());
    }
}
