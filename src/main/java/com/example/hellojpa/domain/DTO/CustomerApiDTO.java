package com.example.hellojpa.domain.DTO;

import javax.validation.constraints.NotBlank;

public record CustomerApiDTO(
        @NotBlank String customerNbr,
        @NotBlank String firstname,
        @NotBlank String lastname) {
}
