package org.duncan.demo.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.duncan.demo.entity.Permission;
import org.duncan.demo.entity.Role;
import org.duncan.demo.entity.User;

import javax.websocket.server.PathParam;
import java.util.List;

@Mapper
public interface UserDao {
    @Select("select * from user where user_Username = #{userUsername} ")
    User loadUserByUsername(String userUsername);

    @Select("select r.role_id,r.role_name from role r,user_role ur where ur.user_id = #{userId} and ur.role_id = r.role_id ")
    List<Role> getRolesByUserId(Integer userId);
    @Select("select p.permission_id,p.permission_url from role_permission rp , permission p where rp.role_id = #{roleId} and p.permission_id = rp.permission_id")
    List<Permission> getPermissionsByRoleId(Integer roleId);

    @Update("update user set user_password = #{userPassword} where user_id = #{userId}")
    Integer updateUserPasswordByUserId(@Param("userPassword")String userPassword, @Param("userId") Integer userId);

}
