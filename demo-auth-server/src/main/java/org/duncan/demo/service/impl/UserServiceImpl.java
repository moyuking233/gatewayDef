package org.duncan.demo.service.impl;

import org.duncan.demo.dao.UserDao;
import org.duncan.demo.entity.Permission;
import org.duncan.demo.entity.Role;
import org.duncan.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    UserDao userDao;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.loadUserByUsername(username);
        if (user == null)throw new UsernameNotFoundException("用户名不存在");
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        List<Role> roles = userDao.getRolesByUserId(user.getUserId());
        for (Role role : roles) {
            //角色必须是ROLE_开头，可以在数据库中设置
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleName().substring(5,role.getRoleName().length()));
            grantedAuthorities.add(grantedAuthority);
            //获取权限
            List<Permission> permissions = userDao.getPermissionsByRoleId(role.getRoleId());
            for (Permission permission : permissions) {
                GrantedAuthority authority = new SimpleGrantedAuthority(permission.getPermissionUrl());
                grantedAuthorities.add(authority);
            }
        }
        user.setGrantedAuthorities(grantedAuthorities);
        return user;
    }

}
