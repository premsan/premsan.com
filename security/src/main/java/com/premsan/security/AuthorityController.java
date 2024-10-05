package com.premsan.security;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class AuthorityController {

    private final AuthorityRepository authorityRepository;

    @GetMapping("/authority-create")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getCreate() {

        final ModelAndView modelAndView = new ModelAndView("authority-create");
        modelAndView.addObject("authority", new AuthorityCreate());

        return modelAndView;
    }

    @PostMapping("/authority-create")
    @PreAuthorize("hasRole('ADMIN')")
    public String postCreate(
            @ModelAttribute("authority") AuthorityCreate create,
            @CurrentSecurityContext SecurityContext securityContext) {

        final Authority authority = new Authority();
        authority.setId(UUID.randomUUID().toString());
        authority.setName(create.getName());
        authority.setUpdatedAt(System.currentTimeMillis());
        authority.setUpdatedBy(securityContext.getAuthentication().getName());

        authorityRepository.save(authority);

        return "index";
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AuthorityCreate {

        private String name;
    }
}
