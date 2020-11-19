package com.lingotrainer.api.domain.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lingotrainer.api.domain.DomainEntity;
import com.lingotrainer.api.domain.model.game.Game;
import com.lingotrainer.api.infrastructure.security.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends DomainEntity implements UserDetails {
    private String username;

    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private int highscore;

    private Role role;

    private int active = 1;

    @ToString.Exclude
    @JsonIgnore
    private List<Game> games = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.role.getValue()));
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.active == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.active == 1;
    }

    @Override
    public boolean isEnabled() {
        return this.active == 1;
    }
}
