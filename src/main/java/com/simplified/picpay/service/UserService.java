package com.simplified.picpay.service;

import com.simplified.picpay.model.domain.user.User;
import com.simplified.picpay.model.domain.user.type.UserType;
import com.simplified.picpay.model.dto.user.UserDTO;
import com.simplified.picpay.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public List<UserDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public UserDTO getUserDtoById(Long id) {
        return toDTO(getUserById(id));
    }

    public User getUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void save(User user) {
        repository.save(user);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public User createUser(UserDTO dto) {
        return repository.save(toEntity(dto));
    }

    public User updateUser(Long id, UserDTO dto) {
        User user = getUserById(id);
        if (dto.firstName() != null) user.setFirstName(dto.firstName());
        if (dto.lastName() != null) user.setLastName(dto.lastName());
        if (dto.document() != null) user.setDocument(dto.document());
        if (dto.email() != null) user.setEmail(dto.email());
        if (dto.password() != null) user.setPassword(dto.password());
        if (dto.balance() != null) user.setBalance(dto.balance());
        if (dto.userType() != null) user.setUserType(dto.userType());
        return repository.save(user);
    }

    public void validateTransaction(User sender, BigDecimal amount) {
        if (sender.getUserType() == UserType.MERCHANT)
            throw new RuntimeException("Merchants cannot make transactions");
        if (sender.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient balance");
    }

    private UserDTO toDTO(User u) {
        return new UserDTO(u.getFirstName(), u.getLastName(), u.getDocument(),
                u.getEmail(), u.getPassword(), u.getBalance(), u.getUserType());
    }

    private User toEntity(UserDTO dto) {
        User u = new User();
        u.setFirstName(dto.firstName());
        u.setLastName(dto.lastName());
        u.setDocument(dto.document());
        u.setEmail(dto.email());
        u.setPassword(dto.password());
        u.setBalance(dto.balance());
        u.setUserType(dto.userType());
        return u;
    }
}