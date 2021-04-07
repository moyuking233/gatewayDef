package org.duncan.demo.controller;

import com.baomidou.mybatisplus.extension.api.R;
import org.duncan.demo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @GetMapping("/user")
    public Principal user(Principal member) {
        return member;
    }

    @DeleteMapping(value = "/exit")
    public R revokeToken(String access_token) {
        R r = new R();
        if (consumerTokenServices.revokeToken(access_token)) {
            r.setCode(200);
            r.setMsg("注销成功");
        } else {
            r.setCode(500);
            r.setMsg("注销失败");
        }
        return r;
    }

}
