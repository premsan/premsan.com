package com.premsan.security.authority;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class AuthorityViewController {

    private final AuthorityRepository authorityRepository;

    @GetMapping("/authority-view/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getCreate(@PathVariable String id) {

        final Optional<Authority> optionalAuthority = authorityRepository.findById(id);

        if (optionalAuthority.isEmpty()) {

            return new ModelAndView("not-found");
        }

        final ModelAndView modelAndView = new ModelAndView("authority-view");
        modelAndView.addObject("authority", optionalAuthority.get());

        return modelAndView;
    }
}
