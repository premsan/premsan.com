package com.premsan.security;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.util.CollectionUtils;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {

        return http.oauth2Login(Customizer.withDefaults())
                .csrf(
                        httpSecurityCsrfConfigurer ->
                                httpSecurityCsrfConfigurer.csrfTokenRepository(
                                        new CookieCsrfTokenRepository()))
                .rememberMe((rememberMe) -> rememberMe.rememberMeServices(rememberMeServices()))
                .build();
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService(
            final SecurityProperties securityProperties,
            final AuthorityRepository authorityRepository,
            final UserRepository userRepository) {
        final OidcUserService delegate = new OidcUserService();

        return (userRequest) -> {
            final OidcUser oidcUser = delegate.loadUser(userRequest);

            final ClientRegistration.ProviderDetails providerDetails =
                    userRequest.getClientRegistration().getProviderDetails();
            final String email =
                    oidcUser.getClaimAsString(
                            providerDetails.getUserInfoEndpoint().getUserNameAttributeName());

            User user = userRepository.findByEmailIgnoreCase(email);

            if (user != null) {

                if (Boolean.TRUE.equals(user.getDisabled())) {

                    throw new DisabledException("User is disabled");
                }
            }
            if (user == null) {

                final String userId = UUID.randomUUID().toString();
                user = new User();
                user.setId(userId);
                user.setEmail(email);
                user.setDisabled(Boolean.FALSE);
                user.setUpdatedAt(System.currentTimeMillis());
                user.setUpdatedBy(userId);

                user = userRepository.save(user);
            }

            final Set<String> admins = securityProperties.getAdmins();

            final Collection<GrantedAuthority> authorities =
                    authorityRepository.findByUserId(user.getId()).stream()
                            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                            .collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(admins) && admins.contains(email)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }

            return new DefaultUser(
                    user.getId(), authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        };
    }

    @Bean
    public SpringSessionRememberMeServices rememberMeServices() {
        final SpringSessionRememberMeServices rememberMeServices =
                new SpringSessionRememberMeServices();
        // optionally customize
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    @Bean
    public NamedParameterJdbcOperations namedParameterJdbcOperations(final DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    static class DefaultUser extends DefaultOidcUser {

        private String userId;

        public DefaultUser(
                final String userId,
                final Collection<? extends GrantedAuthority> authorities,
                final OidcIdToken idToken,
                final OidcUserInfo userInfo) {
            super(authorities, idToken, userInfo);

            this.userId = userId;
        }

        @Override
        public String getName() {
            return userId;
        }
    }
}
