package com.premsan.security.authority;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class AuthorityIndexController {

    private final AuthorityRepository authorityRepository;

    @GetMapping("/authority-index")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAuthorityIndex(
            @RequestParam(required = false) final String id,
            @RequestParam(required = false) final String name,
            @RequestParam(defaultValue = "0") final Integer page) {

        final ModelAndView modelAndView = new ModelAndView("authority-index");

        Page<Authority> authorityPage;

        if (StringUtils.hasText(id)) {

            authorityPage =
                    authorityRepository.findById(id, PageRequest.of(page, 100, Sort.by("id")));
        } else if (StringUtils.hasText(name)) {

            authorityPage =
                    authorityRepository.findByName(
                            name, PageRequest.of(page, 100, Sort.by("name")));
        } else {

            authorityPage = authorityRepository.findAll(PageRequest.of(page, 100, Sort.by("id")));
        }

        modelAndView.addObject("authorityPage", authorityPage);

        return modelAndView;
    }
}
