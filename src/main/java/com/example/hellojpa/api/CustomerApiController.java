package com.example.hellojpa.api;

import com.example.hellojpa.domain.Customer;
import com.example.hellojpa.domain.CustomerMapper;
import com.example.hellojpa.domain.DTO.CustomerApiDTO;
import com.example.hellojpa.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
        ("/api/customer")
public class CustomerApiController {
    private static final Logger log = LoggerFactory.getLogger(CustomerApiController.class);
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    public CustomerApiController(CustomerRepository customerRepository, CustomerMapper customerMapper){
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }
    @GetMapping
    public List<CustomerApiDTO> findAll() {
        return customerRepository.findAllProjectedBy(CustomerApiDTO.class);
    }
    @GetMapping("/{customerNbr}")
    public ResponseEntity<CustomerApiDTO> findByCustomerNbr(
            @PathVariable("customerNbr") String customerNbr
    ) {
        return customerRepository.findByCustomerNbr(customerNbr, CustomerApiDTO.class)
                .map(c -> ResponseEntity.ok(c))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> insert(
            @RequestBody @Valid CustomerApiDTO customerDTO
    ) {
        var customerEntity = customerMapper.map(customerDTO);
        customerRepository.save(customerEntity);
        var uri = MvcUriComponentsBuilder.fromMethodCall(
                MvcUriComponentsBuilder.on(CustomerApiController.class)
                        .findByCustomerNbr(customerDTO.customerNbr())
        ).build();
        return ResponseEntity.created(uri.toUri()).build();
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleConstraintViolation(DataIntegrityViolationException x) {
        log.error("Constraint Violation", x);
    }



}

