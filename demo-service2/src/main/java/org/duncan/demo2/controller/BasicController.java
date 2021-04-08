package org.duncan.demo2.controller;

import org.duncan.demo2.service.FeignCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class BasicController {
    @Autowired
    FeignCall feignCall;

    @GetMapping("/hello")
    public String hello(){
        String hello = feignCall.hello();
        System.out.println("hello ====== : " + hello);
        return hello;
    }

    @GetMapping("/current")
    public Principal user(Principal principal) {
        return principal;
    }

    @GetMapping("/query")
    @PreAuthorize("hasAnyAuthority('admin')")
    public String query() {
        return "具有query权限";
    }
}
