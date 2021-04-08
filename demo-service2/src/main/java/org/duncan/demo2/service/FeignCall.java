package org.duncan.demo2.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient("EHR-BASIC-SERVICE")
public interface FeignCall {
    @GetMapping("/api/hello")
    public String hello();
}
