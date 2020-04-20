package de.netos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.netos.model.User;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUsername(String username);
    
    boolean existsByUsername(String useraname);
    
    void deleteByUsername(String username);
}
