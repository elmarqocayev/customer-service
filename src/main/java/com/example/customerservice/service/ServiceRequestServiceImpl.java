package com.example.customerservice.service;

import com.example.customerservice.domain.Customer;
import com.example.customerservice.domain.RequestStatus;
import com.example.customerservice.domain.ServiceRequest;
import com.example.customerservice.domain.Tag;
import com.example.customerservice.dto.ServiceRequestCreate;
import com.example.customerservice.dto.ServiceRequestResponse;
import com.example.customerservice.event.ServiceRequestStatusChangedEvent;
import com.example.customerservice.exception.BusinessRuleException;
import com.example.customerservice.exception.ResourceNotFoundException;
import com.example.customerservice.repository.CustomerRepository;
import com.example.customerservice.repository.ServiceRequestRepository;
import com.example.customerservice.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServiceRequestServiceImpl implements ServiceRequestService {

    private final ServiceRequestRepository requestRepository;
    private final CustomerRepository customerRepository;
    private final TagRepository tagRepository;
    private final ApplicationEventPublisher eventPublisher;

    /** Addım 1: müraciət yaradılır (status = OPEN). Müraciət və tag-lar eyni transaction-da saxlanır. */
    @Override
    @Transactional
    public ServiceRequestResponse create(ServiceRequestCreate dto) {
        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Müştəri", dto.customerId()));

        ServiceRequest request = new ServiceRequest();
        request.setTitle(dto.title());
        request.setDescription(dto.description());
        request.setPriority(dto.priority());
        request.setStatus(RequestStatus.OPEN);
        request.setCustomer(customer);
        request.setTags(resolveTags(dto.tags()));

        ServiceRequest saved = requestRepository.save(request);
        eventPublisher.publishEvent(new ServiceRequestStatusChangedEvent(
                saved.getId(), customer.getEmail(), null, RequestStatus.OPEN));
        return ServiceRequestResponse.from(saved);
    }

    @Override
    public ServiceRequestResponse get(Long id) {
        return ServiceRequestResponse.from(find(id));
    }

    @Override
    public Page<ServiceRequestResponse> list(RequestStatus status, Pageable pageable) {
        Page<ServiceRequest> page = (status == null)
                ? requestRepository.findAll(pageable)
                : requestRepository.findByStatus(status, pageable);
        return page.map(ServiceRequestResponse::from);
    }

    /** Addım 2..N: status dəyişir. Qayda pozulsa exception atılır və transaction geri qaytarılır (rollback). */
    @Override
    @Transactional
    public ServiceRequestResponse changeStatus(Long id, RequestStatus newStatus) {
        ServiceRequest request = find(id);
        RequestStatus old = request.getStatus();

        if (!old.canTransitionTo(newStatus)) {
            throw new BusinessRuleException(
                    "Statusu " + old + " -> " + newStatus + " dəyişmək olmaz");
        }
        request.setStatus(newStatus);

        // Hadisə yalnız COMMIT-dən sonra dinlənilir (bax: NotificationListener)
        eventPublisher.publishEvent(new ServiceRequestStatusChangedEvent(
                request.getId(), request.getCustomer().getEmail(), old, newStatus));
        return ServiceRequestResponse.from(request);
    }

    private ServiceRequest find(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Müraciət", id));
    }

    private Set<Tag> resolveTags(Set<String> names) {
        Set<Tag> result = new HashSet<>();
        if (names == null) {
            return result;
        }
        for (String raw : names) {
            if (raw == null || raw.isBlank()) {
                continue;
            }
            String name = raw.trim().toLowerCase(Locale.ROOT);
            Tag tag = tagRepository.findByNameIgnoreCase(name)
                    .orElseGet(() -> tagRepository.save(new Tag(name)));
            result.add(tag);
        }
        return result;
    }
}
