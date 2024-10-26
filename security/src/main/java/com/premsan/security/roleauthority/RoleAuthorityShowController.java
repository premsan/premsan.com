package com.premsan.security.roleauthority;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class RoleAuthorityShowController {

    private final RoleAuthorityRepository roleAuthorityRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/role-authority-show/{id}")
    public ModelAndView getCreate(@PathVariable final String id) {

        Optional<RoleAuthority> roleAuthorityOptional = roleAuthorityRepository.findById(id);

        if (roleAuthorityOptional.isEmpty()) {

            return new ModelAndView("not-found");
        }

        final ModelAndView modelAndView = new ModelAndView("role-authority-show-page");
        modelAndView.addObject("roleAuthority", roleAuthorityOptional.get());

        return modelAndView;
    }
}
