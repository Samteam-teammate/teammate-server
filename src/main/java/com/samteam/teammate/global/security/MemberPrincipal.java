package com.samteam.teammate.global.security;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberPrincipal implements UserDetails, Serializable {
	private final String id;
	private final Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return id;
	}
}
