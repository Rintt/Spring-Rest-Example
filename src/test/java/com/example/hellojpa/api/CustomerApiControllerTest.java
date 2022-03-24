package com.example.hellojpa.api;

import com.example.hellojpa.domain.Customer;
import com.example.hellojpa.domain.DTO.CustomerApiDTO;
import com.example.hellojpa.implementation.CustomerMapperImpl;
import com.example.hellojpa.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@WebMvcTest(CustomerApiController.class)
@Import(CustomerMapperImpl.class)
class CustomerApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CustomerRepository customerRepository;

    @Test
    void testFindAll() throws Exception {
        var customer = Customer.builder()
                .firstname("Test")
                .lastname("Mensch")
                .id(UUID.randomUUID())
                .customerNbr("C001")
                .build();
        Mockito.when(customerRepository.findAll()).thenReturn(List.of(customer));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstname")
                        .value(customer.getFirstname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastname")
                        .value(customer.getLastname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerNbr")
                        .value(customer.getCustomerNbr()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id")
                        .value(customer.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orders").isArray());
        Mockito.verify(customerRepository).findAll();
    }

    @Test
    void testFindAll2() throws Exception {
        var customer = new CustomerApiDTO("C001", "Test", "Mensch");
        Mockito.when(customerRepository.findAllProjectedBy(CustomerApiDTO.class)).thenReturn(List.of(customer));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstname")
                        .value(customer.firstname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastname")
                        .value(customer.lastname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerNbr")
                        .value(customer.customerNbr()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orders").doesNotExist());
        Mockito.verify(customerRepository).findAllProjectedBy(CustomerApiDTO.class);
    }

    @Test
    void testFindByCustomerNbr() throws Exception {
        var customer = new CustomerApiDTO("C001", "Test", "Mensch");
        Mockito.when(customerRepository.findByCustomerNbr(customer.customerNbr(), CustomerApiDTO.class))
                .thenReturn(Optional.of(customer));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customer/{id}", customer.customerNbr()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname")
                        .value(customer.firstname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname")
                        .value(customer.lastname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerNbr")
                        .value(customer.customerNbr()));
        Mockito.verify(customerRepository).findByCustomerNbr(customer.customerNbr(), CustomerApiDTO.class);
    }

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testInsert() throws Exception {
        var customer = new CustomerApiDTO("C001", "Test", "Mensch");
        var mockCustomerEntity = Customer.builder()
                .id(UUID.randomUUID())
                .firstname(customer.firstname())
                .lastname(customer.lastname())
                .customerNbr(customer.customerNbr())
                .build();
        Mockito.when(customerRepository.save(ArgumentMatchers.any())).thenReturn(mockCustomerEntity);
        var json = objectMapper.writeValueAsString(customer);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
        var argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerRepository).save(argumentCaptor.capture());
        var savedCustomer = argumentCaptor.getValue();
        Assertions.assertThat(customer).usingRecursiveComparison().isEqualTo(savedCustomer);

    }
    @Test
    void testInsertValidates() throws Exception {
        var customer = new CustomerApiDTO("C001", "", "Mensch");
        var json = objectMapper.writeValueAsString(customer);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void testNotFound() throws Exception {
        var unknown = "unknown";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customer/{customerNbr}", unknown))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        Mockito.verify(customerRepository).findByCustomerNbr(unknown, CustomerApiDTO.class);
    }
    @Test
    void testInsert2() throws Exception {
        var customer = new CustomerApiDTO("C001", "Test", "Mensch");
        var mockCustomerEntity = Customer.builder()
                .id(UUID.randomUUID())
                .firstname(customer.firstname())
                .lastname(customer.lastname())
                .customerNbr(customer.customerNbr())
                .build();
        Mockito.when(customerRepository.save(ArgumentMatchers.any())).thenReturn(mockCustomerEntity);
        var json = objectMapper.writeValueAsString(customer);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header()
                        .string("Location",
                                "http://localhost/api/customer/" + customer.customerNbr()));
        var argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerRepository).save(argumentCaptor.capture());
        var savedCustomer = argumentCaptor.getValue();
        Assertions.assertThat(customer).usingRecursiveComparison().isEqualTo(savedCustomer);
    }



}