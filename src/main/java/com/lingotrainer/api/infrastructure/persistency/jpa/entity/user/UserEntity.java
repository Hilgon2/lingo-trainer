package com.lingotrainer.api.infrastructure.persistency.jpa.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.PersistableEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.GameEntity;
import com.lingotrainer.api.infrastructure.security.Role;
import com.lingotrainer.api.infrastructure.security.json.MyJsonView;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends PersistableEntity implements UserDetails {

    @NotEmpty(message = "Gebruikersnaam mag niet leeg zijn")
    @Pattern(regexp="^([a-z]|[A-Z]|[-]|[0-9])*$", message = "Gebruikersnaam mag enkel letters en cijfers bevatten")
    @Column(unique=true)
    private String username;

    @NotEmpty(message = "Wachtwoord mag niet leeg zijn")
    @Pattern(regexp="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Wachtwoord moet minimaal 8 tekens, een kleine letter, een hoofdletter, een getal en een speciaal teken bevatten")
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private int highscore;

    @Enumerated(value = EnumType.STRING)
    @JsonView(MyJsonView.Admin.class)
    private Role role;

    private int active = 1;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    @JsonIgnore
    @ToString.Exclude
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private List<GameEntity> games = new ArrayList<>();

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
