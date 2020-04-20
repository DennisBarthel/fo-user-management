package de.netos.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import de.netos.util.LocalDate2DateAttributeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "firstname")
	private String firstname;
	
	@Column(name = "lastname")
	private String lastname;
	
	@Column(name = "credentialsExpiryDate")
	@Convert(converter = LocalDate2DateAttributeConverter.class)
	private LocalDate credentialsExpiryDate;
	
	@Column(name = "enabled")
    private boolean enabled;
	
    @Column(name = "accountNonExpired")
    private boolean accountNonExpired;
    
    @Column(name = "credentialsNonExpired")
    private boolean credentialsNonExpired;
    
    @Column(name = "accountNonLocked")
    private boolean accountNonLocked;
    
    @ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_authority",
			joinColumns = {@JoinColumn(name = "userId")},
			inverseJoinColumns = {@JoinColumn(name = "authorityId")})
	private List<Authority> authorities = new ArrayList<>();
    
    public void addAuthority(Authority authority) {
		authorities.add(authority);
		authority.getUsers().add(this);
	}
	
	public void removeAuthority(Authority authority) {
		authorities.remove(authority);
		authority.getUsers().remove(this);
	}
}
