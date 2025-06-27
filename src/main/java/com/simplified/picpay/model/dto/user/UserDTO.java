package com.simplified.picpay.model.dto.user;

import java.math.BigDecimal;

public record UserDTO(String firstName, String lastName, String email, BigDecimal balance) {
}
