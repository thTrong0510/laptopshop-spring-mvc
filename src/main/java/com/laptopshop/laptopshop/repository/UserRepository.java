package com.laptopshop.laptopshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.laptopshop.laptopshop.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);

    List<User> findByEmail(String email);

    List<User> findByEmailAndAddress(String email, String Address);

    Page<User> findAll(Pageable pageable);

    Optional<User> findById(long id);

    void deleteById(long id);

    Boolean existsByEmail(String email);

    User findOneByEmail(String email);

    long count();

}
