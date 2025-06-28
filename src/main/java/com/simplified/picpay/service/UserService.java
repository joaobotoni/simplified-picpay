package com.simplified.picpay.service;

import com.simplified.picpay.model.domain.user.User;
import com.simplified.picpay.model.dto.user.UserDTO;
import com.simplified.picpay.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<UserDTO> getAll() {
        return repository.findAll().stream()
                .map(user -> new UserDTO(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getBalance())).toList();
    }

    public Optional<UserDTO> getUserById(Long id) {
        return repository.findById(id).map(user -> new UserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBalance()));
    }

    public void create(User user) {
        repository.save(user);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
