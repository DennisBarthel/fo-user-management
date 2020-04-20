package de.netos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.netos.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	Optional<Authority> findByName(String name);
}
