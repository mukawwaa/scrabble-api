package com.gamecity.scrabble.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.security.core.userdetails.UserDetails;

@Entity(name = "User")
@Table(name = "users")
@NamedQuery(name = "findByUsername", query = "Select u from User u where u.username = :username")
public class User extends AbstractEntity implements UserDetails
{
    private static final long serialVersionUID = 7582993979095846948L;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false, columnDefinition = "tinyint default 1")
    private boolean enabled;

    @Column(name = "account_non_expired", nullable = false, columnDefinition = "tinyint default 1")
    private boolean accountNonExpired;

    @Column(name = "account_non_locked", nullable = false, columnDefinition = "tinyint default 1")
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired", nullable = false, columnDefinition = "tinyint default 1")
    private boolean credentialsNonExpired;

    @Transient
    private Collection<? extends BaseAuthority> authorities;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSurname()
    {
        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isAccountNonExpired()
    {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired)
    {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked()
    {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked)
    {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired()
    {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired)
    {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Collection<? extends BaseAuthority> getAuthorities()
    {
        return authorities;
    }

    public void setAuthorities(Collection<? extends BaseAuthority> authorities)
    {
        this.authorities = authorities;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
