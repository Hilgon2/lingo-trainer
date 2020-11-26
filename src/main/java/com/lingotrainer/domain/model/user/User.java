package com.lingotrainer.domain.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lingotrainer.domain.model.game.GameId;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    private transient UserId userId;

    private String username;

    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private int highscore;

    private Role role;

    private int active = 1;

    @ToString.Exclude
    @JsonIgnore
    private transient List<GameId> gameIds = new ArrayList<>();

    public int getUserId() {
        if (this.userId == null) {
            return 0;
        }
        return this.userId.getId();
    }

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
