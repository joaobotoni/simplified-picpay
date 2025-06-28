package com.simplified.picpay.service;

import com.simplified.picpay.model.domain.user.User;
import com.simplified.picpay.model.domain.user.type.UserType;
import com.simplified.picpay.model.dto.user.UserDTO;
import com.simplified.picpay.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<UserDTO> getAll() {
        return this.repository.findAll().stream()
                .map(user -> new UserDTO(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getBalance())).toList();
    }

    public UserDTO getUserDtoById(Long id) {
        return this.repository.findById(id).map(user -> new UserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBalance())).orElseThrow(RuntimeException::new);
    }


    public User getUserById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new RuntimeException("User notfound"));
    }

    public void create(User user) {
        this.repository.save(user);
    }

    public void delete(Long id) {
        this.repository.deleteById(id);
    }


        public void validateTransaction(User sender, BigDecimal balance){
        if(sender.getUserType() == UserType.MERCHANT){
            throw new RuntimeException("Esse tipo de usuario não está autorizado a fazer transação");
        }
        if(sender.getBalance().compareTo(balance) < 0){
            throw new RuntimeException("Saldo insuficiente");
        }
    }

}
