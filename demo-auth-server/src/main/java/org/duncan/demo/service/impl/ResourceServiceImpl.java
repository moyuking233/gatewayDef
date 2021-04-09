package org.duncan.demo.service.impl;

import org.duncan.demo.constant.RedisConstant;
import org.duncan.demo.dao.UserDao;
import org.duncan.demo.entity.Permission;
import org.duncan.demo.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 资源与角色匹配关系管理业务类
 * Created by macro on 2020/6/19.
 */
@Service
public class ResourceServiceImpl {

    private Map<String, List<String>> resourceRolesMap;
    @Autowired
    UserDao userDao;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @PostConstruct
    public void initData() {
        resourceRolesMap = new TreeMap<>();
        List<Permission> permissions = userDao.getAllPermissions();
        for (Permission permission : permissions) {
            String url = permission.getPermissionUrl();
            List<Role> roles = userDao.getRolesByPermissionId(permission.getPermissionId());
            ArrayList<String> rolesNameList = new ArrayList<>();
            for (Role role : roles) {
                rolesNameList.add(role.getRoleName());
            }
            resourceRolesMap.put(url,rolesNameList);
        }
        redisTemplate.opsForHash().putAll(RedisConstant.RESOURCE_ROLES_MAP, resourceRolesMap);
    }
}
