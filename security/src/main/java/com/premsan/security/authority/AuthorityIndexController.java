package com.premsan.security.authority;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class AuthorityIndexController {

    private final AuthorityRepository authorityRepository;

    @GetMapping("/authority-index")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAuthorityIndex(@RequestParam(defaultValue = "0") final Integer page) {

        final ModelAndView modelAndView = new ModelAndView("authority-index");
        modelAndView.addObject(
                "authorities",
                authorityRepository.findAll(PageRequest.of(page, 100, Sort.by("id"))));

        return modelAndView;
    }

    @GetMapping("/authority-index-find-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAuthorityIndexFindById(
            @RequestParam String id, @RequestParam(defaultValue = "0") final Integer page) {

        final ModelAndView modelAndView = new ModelAndView("authority-index");
        modelAndView.addObject(
                "authorities",
                authorityRepository.findById(id, PageRequest.of(page, 100, Sort.by("id"))));

        return modelAndView;
    }

    @GetMapping("/authority-index-find-by-name")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAuthorityIndexFindByName(
            @RequestParam final String name, @RequestParam(defaultValue = "0") final Integer page) {

        final ModelAndView modelAndView = new ModelAndView("authority-index");
        modelAndView.addObject(
                "authorities",
                authorityRepository.findByName(name, PageRequest.of(page, 100, Sort.by("name"))));

        return modelAndView;
    }
}
