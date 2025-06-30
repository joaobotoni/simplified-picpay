package com.simplified.picpay.service;

import com.simplified.picpay.model.domain.user.User;
import com.simplified.picpay.model.domain.user.type.UserType;
import com.simplified.picpay.model.dto.user.UserDTO;
import com.simplified.picpay.model.mapper.UserMapper;
import com.simplified.picpay.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public List<UserDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::mapUserToUserDto)
                .toList();
    }

    public UserDTO getUserDtoById(Long id) {
        return mapper.mapUserToUserDto(getUserById(id));
    }

    public User getUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void save(User user) {
        repository.save(user);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public User createUser(UserDTO dto) {
        return repository.save(mapper.mapUserDtoToUser(dto));
    }

    public User updateUser(Long id, UserDTO dto) {
        User user = getUserById(id);
        mapper.updateUserFromDto(dto, user);
        return repository.save(user);
    }

    public void validateTransaction(User sender, BigDecimal amount) {
        if (sender.getUserType() == UserType.MERCHANT)
            throw new RuntimeException("Merchants cannot make transactions");

        if (sender.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient balance");
    }
}
