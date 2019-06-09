package com.ajulay.repository;

import com.ajulay.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    User findByActivationCode(String code);

    List<User> findAll();
}
