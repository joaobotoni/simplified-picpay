package com.simplified.picpay.model.repository;

import com.simplified.picpay.model.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>  {
}
