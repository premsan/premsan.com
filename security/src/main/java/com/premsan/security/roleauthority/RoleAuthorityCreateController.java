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
import org.springframework.util.StringUtils;
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

        final ModelAndView modelAndView = new ModelAndView("role-authority-create");
        modelAndView.addObject("roleAuthority", roleAuthorityCreate);

        return modelAndView;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/role-authority-create")
    public ModelAndView postCreate(
            @Valid @ModelAttribute("roleAuthority") RoleAuthorityCreate roleAuthorityCreate,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        ModelAndView modelAndView = new ModelAndView();

        Role role = null;

        if (StringUtils.hasText(roleAuthorityCreate.getRoleId())) {

            role = roleRepository.findById(roleAuthorityCreate.getRoleId()).orElse(null);
        }

        if (role == null) {

            //
            // roleAuthorityCreate.setRoleIdError(RoleAuthorityCreate.Error.INVALID_ROLE_ID);
        }

        Authority authority = null;

        if (StringUtils.hasText(roleAuthorityCreate.getAuthorityId())) {

            authority =
                    authorityRepository.findById(roleAuthorityCreate.getAuthorityId()).orElse(null);
        }

        if (authority == null) {

            //
            // roleAuthorityCreate.setAuthorityIdError(RoleAuthorityCreate.Error.INVALID_AUTHORITY_ID);
        }

        if (bindingResult.hasErrors()) {

            modelAndView.setViewName("role-authority-create");
            modelAndView.addObject("roleAuthority", roleAuthorityCreate);

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

        @NotEmpty(message = "INVALID_AUTHORITY_ID")
        private String authorityId;

        private Error authorityIdError;

        @NotEmpty(message = "INVALID_AUTHORITY_ID")
        private String roleId;

        private Error roleIdError;

        enum Error {
            INVALID_AUTHORITY_ID,
            INVALID_ROLE_ID
        }
    }
}
