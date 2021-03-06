package org.duncan.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class BasicController {

    @GetMapping("/hello")
    public String hello(){
        return "hello";
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
