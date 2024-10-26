package com.premsan.security.roleauthority;

import com.premsan.security.Role;
import com.premsan.security.authority.Authority;
import com.premsan.security.authority.AuthorityRepository;
import com.premsan.security.role.RoleRepository;
import java.util.HashSet;
import java.util.Set;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class RoleAuthorityCreateController {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    private final RoleAuthorityRepository roleAuthorityRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/role-authority-create")
    public ModelAndView getCreate(final RoleAuthorityCreate roleAuthorityCreate) {

        final ModelAndView modelAndView = new ModelAndView("role-authority-create-page");
        modelAndView.addObject("roleAuthority", roleAuthorityCreate);

        return modelAndView;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/role-authority-create")
    public ModelAndView postCreate(
            final RoleAuthorityCreate roleAuthorityCreate,
            @CurrentSecurityContext SecurityContext securityContext) {

        final ModelAndView modelAndView = new ModelAndView();

        Role role = null;

        if (StringUtils.hasText(roleAuthorityCreate.getRoleId())) {

            role = roleRepository.findById(roleAuthorityCreate.getRoleId()).orElse(null);
        }

        if (role == null) {

            roleAuthorityCreate.getErrors().add(RoleAuthorityCreate.Error.INVALID_ROLE_ID);
        }

        Authority authority = null;

        if (StringUtils.hasText(roleAuthorityCreate.getAuthorityId())) {

            authority =
                    authorityRepository.findById(roleAuthorityCreate.getAuthorityId()).orElse(null);
        }

        if (authority == null) {

            roleAuthorityCreate.getErrors().add(RoleAuthorityCreate.Error.INVALID_AUTHORITY_ID);
        }

        if (!roleAuthorityCreate.getErrors().isEmpty()) {

            modelAndView.setViewName("role-authority-create-fragment");
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

        modelAndView.setViewName("role-authority-show-fragment");
        modelAndView.addObject("roleAuthority", roleAuthority);

        return modelAndView;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public class RoleAuthorityCreate {

        private String authorityId;

        private String roleId;

        private Set<Error> errors = new HashSet<>();

        enum Error {
            INVALID_AUTHORITY_ID,
            INVALID_ROLE_ID
        }
    }
}
