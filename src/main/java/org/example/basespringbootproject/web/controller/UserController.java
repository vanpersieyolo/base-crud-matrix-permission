package org.example.basespringbootproject.web.controller;

import org.example.basespringbootproject.application.dto.UserDTO;
import org.example.basespringbootproject.application.service.IBaseService;
import org.example.basespringbootproject.application.service.IUserService;
import org.example.basespringbootproject.infrastructure.aop.annotation.Loggable;
import org.example.basespringbootproject.infrastructure.aop.annotation.PerformanceTrack;
import org.example.basespringbootproject.infrastructure.security.userdetails.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractBaseController<UserDTO, Long> {
    @Autowired
    IUserService userService;

    protected UserController(IBaseService<UserDTO, Long> service) {
        super(service);
    }

    //demo logging aspect
    @GetMapping("/hello")
    @Loggable
    public String hello() {
        return "Hello, User!";
    }

    //demo performance aspect
    @GetMapping("/good-performance")
    @PerformanceTrack(threshold = 2000)
    public String goodPerformance() throws InterruptedException {
        //simulating good performance
        Thread.sleep(1999);
        return "This method has good performance!";
    }

    @GetMapping("/bad-performance")
    @PerformanceTrack(threshold = 2000)
    public String badPerformance() throws InterruptedException {
        //simulating bad performance
        Thread.sleep(2001);
        return "This method has bad performance!";
    }

    @GetMapping("/me")
    public UserDTO me(@AuthenticationPrincipal CustomUserDetails principal) {
        return userService.getByUserName(principal.getUsername());
    }


}