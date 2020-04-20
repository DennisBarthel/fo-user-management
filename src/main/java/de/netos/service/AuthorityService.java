package de.netos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import de.netos.model.Authority;
import de.netos.repository.AuthorityRepository;

@Service
public class AuthorityService {

	@Autowired
	private AuthorityRepository repository;
	
	@Cacheable("authorities")
	public Authority getAuthorityByName(String name) {
		return repository.findByName("USER")
				.orElseThrow(() -> new RuntimeException("No authority found"));
	}
}
