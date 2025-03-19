package com.adi.main.repository;

import org.springframework.data.repository.CrudRepository;

import com.adi.main.model.User;

public interface UserRepository extends CrudRepository<User, Integer>{

}
