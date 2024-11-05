package com.premsan.security.roleauthority;

import com.premsan.security.Role;
import com.premsan.security.authority.Authority;
import com.premsan.security.authority.AuthorityRepository;
import com.premsan.security.role.RoleRepository;
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
public class RoleAuthorityCreateController {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    private final RoleAuthorityRepository roleAuthorityRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/role-authority-create")
    public ModelAndView getCreate(final RoleAuthorityCreate roleAuthorityCreate) {

        final ModelAndView modelAndView = new ModelAndView("com/premsan/security/templates/role-authority-create");
        modelAndView.addObject("roleAuthorityCreate", roleAuthorityCreate);

        return modelAndView;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/role-authority-create")
    public ModelAndView postCreate(
            @Valid @ModelAttribute("roleAuthorityCreate") RoleAuthorityCreate roleAuthorityCreate,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {

            modelAndView.setViewName("com/premsan/security/templates/role-authority-create");
            modelAndView.addObject("roleAuthorityCreate", roleAuthorityCreate);

            return modelAndView;
        }

        final Role role = roleRepository.findById(roleAuthorityCreate.getRoleId()).orElse(null);

        if (role == null) {

            bindingResult.rejectValue("roleId", "Invalid");
        }

        final Authority authority  = authorityRepository.findById(roleAuthorityCreate.getAuthorityId()).orElse(null);

        if (authority == null) {

            bindingResult.rejectValue("authorityId", "Invalid");
        }

        if (bindingResult.hasErrors()) {

            modelAndView.setViewName("com/premsan/security/templates/role-authority-create");
            modelAndView.addObject("roleAuthorityCreate", roleAuthorityCreate);

            return modelAndView;
        }

        final RoleAuthority roleAuthority =
                roleAuthorityRepository.save(
                        new RoleAuthority(
                                UUID.randomUUID().toString(),
                                null,
                                role.getId(),
                                authority.getId(),
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName()));

        redirectAttributes.addAttribute("id", roleAuthority.getId());
        return new ModelAndView("redirect:/role-authority-view/{id}");
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public class RoleAuthorityCreate {

        @NotEmpty
        private String roleId;

        @NotEmpty
        private String authorityId;
    }
}
