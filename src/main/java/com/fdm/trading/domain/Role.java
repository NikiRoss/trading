package com.fdm.trading.domain;

import javax.persistence.*;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long roleId;

    @Column(name="role_name")
    private String roleName;

    @Column(name="username")
    private String username;
}
