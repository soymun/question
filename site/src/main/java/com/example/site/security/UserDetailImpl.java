package com.example.site.security;

import com.example.site.model.util.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailImpl implements UserDetails {

    private Long id;

    private String email;

    private String password;

    private Role role;

    private List<? extends GrantedAuthority> grantedAuthorities;

    private Boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return enabled;
    }

    public boolean isAdmin() {
        return grantedAuthorities.stream().anyMatch(p -> p.getAuthority().equals("ADMIN"));
    }

    public boolean isTeacher() {
        return grantedAuthorities.stream().anyMatch(p -> p.getAuthority().equals("TEACHER"));
    }
}
