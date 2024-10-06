package com.premsan.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView index() {

        final ModelAndView modelAndView = new ModelAndView("user-list");
        modelAndView.addObject("users", userRepository.findAll());

        return modelAndView;
    }
}
