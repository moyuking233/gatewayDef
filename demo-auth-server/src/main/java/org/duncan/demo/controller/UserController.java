package org.duncan.demo.controller;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import org.duncan.demo.entity.User;
import org.duncan.demo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @GetMapping("/user")
    public User user() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @DeleteMapping(value = "/exit")
    public R revokeToken(String jwt) {
        R r = new R();
        if (consumerTokenServices.revokeToken(jwt)) {
            r.setCode(200);
            r.setMsg("注销成功");
        }
        r.setCode(500);
        r.setMsg("注销失败");
        return r;
    }

}
