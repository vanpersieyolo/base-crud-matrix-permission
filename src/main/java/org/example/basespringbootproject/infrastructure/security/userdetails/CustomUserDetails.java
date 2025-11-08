package org.example.basespringbootproject.infrastructure.security.userdetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.basespringbootproject.domain.model.Role;
import org.example.basespringbootproject.domain.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<String> rolesAndPerms = new HashSet<>();

        for (Role role : user.getRoles()) {
            rolesAndPerms.add("ROLE_" + role.getCode());
            rolesAndPerms.addAll(
                    role.getPermissions().stream()
                            .map(perm -> "PERM_" + perm.getCode())
                            .collect(Collectors.toSet())
            );
        }

        return rolesAndPerms.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
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
        return user.isActive();
    }
}
