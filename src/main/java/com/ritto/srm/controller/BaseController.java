package com.ritto.srm.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Date;
import java.util.Map;

/**
 * @author : Eiden J.P Zhou
 * @Date: 2018/4/11
 * @Description:
 * @Modified By:
 */

@Controller
@RequestMapping("/")
public class BaseController implements WebMvcConfigurer {
    @GetMapping("/home")
    public String home(Map<String, Object> model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        model.put("message", "Hello World  "+userDetails.getUsername());
        model.put("title", "Hello Home");
        model.put("date", new Date());
        return "home";
    }

    @GetMapping("/index")
    public String index(Map<String, Object> model){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        model.put("username",userDetails.getUsername());
        return "index";
    }

    @RequestMapping("/foo")
    public String foo() {
        throw new RuntimeException("Expected exception in controller");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

}
