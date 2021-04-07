package org.duncan.demo.entity;

import lombok.Data;

import java.util.List;

@Data
public class Role {
    private int roleId;
    private String roleName;
    private List<Permission> permissions;
}
