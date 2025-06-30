// DTO único para todas as operações
package com.simplified.picpay.model.dto.user;

import com.simplified.picpay.model.domain.user.type.UserType;
import java.math.BigDecimal;

public record UserDTO(
        Long id,
        String firstName,
        String lastName,
        String document,
        String email,
        String password,
        BigDecimal balance,
        UserType userType
) {}