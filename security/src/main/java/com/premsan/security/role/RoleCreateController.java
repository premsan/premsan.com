package com.premsan.security.role;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class RoleCreateController {

    private final RoleRepository roleRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/security/role-create")
    public ModelAndView getCreate(final RoleCreate roleCreate) {

        final ModelAndView modelAndView =
                new ModelAndView("com/premsan/security/templates/role-create");
        modelAndView.addObject("roleCreate", roleCreate);

        return modelAndView;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/security/role-create")
    public ModelAndView postCreate(
            @Valid @ModelAttribute("roleCreate") RoleCreate roleCreate,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {

            modelAndView.setViewName("com/premsan/security/templates/role-create");
            modelAndView.addObject("roleCreate", roleCreate);

            return modelAndView;
        }

        final Role role =
                roleRepository.save(
                        new Role(
                                UUID.randomUUID().toString(),
                                null,
                                roleCreate.getName(),
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName()));

        redirectAttributes.addAttribute("id", role.getId());
        return new ModelAndView("redirect:/security/role-view/{id}");
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public class RoleCreate {

        @NotEmpty private String name;
    }
}
