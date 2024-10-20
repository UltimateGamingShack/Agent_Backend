package com.newgen.agent.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.newgen.agent.dto.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude= {"password", "mpin"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Agent implements UserDetails {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer agentId;
	private String password;
	@Enumerated(EnumType.STRING)
	private Role role;
	private String agentName;
	private String mpin;
	@Column(unique=true)
	private String agencyCode;
	
	 @Override
	    public String getUsername() {
	        return agencyCode;
	    }

	    @Override
	    public boolean isAccountNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isAccountNonLocked() {
	        return true;
	    }

	    @Override
	    public boolean isCredentialsNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isEnabled() {
	        return true;
	    }

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			// TODO Auto-generated method stub
			return List.of();
		}
}
