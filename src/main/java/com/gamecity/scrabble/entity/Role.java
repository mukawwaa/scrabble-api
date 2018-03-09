package com.gamecity.scrabble.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity(name = "Role")
@Table(name = "roles", uniqueConstraints = { @UniqueConstraint(name = "UK_ROLE_NAME", columnNames = "rolename") })
@NamedQuery(name = "findByRoleName", query = "Select r from Role r where r.rolename = :rolename")
public class Role extends AbstractEntity
{
    @Column(name = "rolename", nullable = false)
    private String rolename;

    @Column(name = "enabled", nullable = false, columnDefinition = "tinyint default 1")
    private boolean enabled;

    public String getRolename()
    {
        return rolename;
    }

    public void setRolename(String rolename)
    {
        this.rolename = rolename;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
