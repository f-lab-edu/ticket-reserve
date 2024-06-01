package com.kjh.core.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class SecurityUser extends User {
    public SecurityUser(String username, Collection<? extends GrantedAuthority> authorities) {
        super(username, "", authorities);
    }
}
