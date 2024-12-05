package com.medical.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medical.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByIdAndRole(long userId, String name);

	User findByUsername(String username);

	User findByUsernameAndPassword(String username, String password);

	List<User> findAllByIdIn(List<Long> users);

}
