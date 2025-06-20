package com.farmsure.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column
    private String phone;

    @Column
    private String address;

    @Column(name = "theme_preference")
    private String themePreference;

    @Column(name = "language_preference")
    private String languagePreference;

    @Column(name = "email_notifications")
    private Boolean emailNotifications = true;

    @Column(name = "sms_notifications")
    private Boolean smsNotifications = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return Collections.singletonList(new SimpleGrantedAuthority(roleWithPrefix));
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
}
