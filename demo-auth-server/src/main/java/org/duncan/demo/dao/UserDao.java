package org.duncan.demo.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.duncan.demo.entity.Role;
import org.duncan.demo.entity.User;

import java.util.List;

@Mapper
public interface UserDao {
    @Select("select * from role where user_Username = #{userUsername} ")
    User loadUserByUsername(String userUsername);

    @Select("select * from role where user_id = #{userId} ")
    List<Role> getRolesByUserId(Integer userId);

    @Update("update user set user_password = #{userPassword} where userId = #{userId}")
    Integer updateUserPasswordByUserId(String userPassword,Integer userId);
}
