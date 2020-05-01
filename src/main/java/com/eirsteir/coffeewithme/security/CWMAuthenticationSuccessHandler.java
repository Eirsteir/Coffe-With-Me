package com.eirsteir.coffeewithme.security;

import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.exception.CWMException;
import com.eirsteir.coffeewithme.exception.ExceptionType;
import com.eirsteir.coffeewithme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.eirsteir.coffeewithme.exception.EntityType.USER;

@Component
public class CWMAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final String BASE_URL = "http://localhost:8080/";

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        if (response.isCommitted())
            return;

        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        Map<String, Object> attributes = oidcUser.getAttributes();
        String email = (String) attributes.get("email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> CWMException.throwException(
                        USER, ExceptionType.ENTITY_NOT_FOUND, email));

        String token = JwtTokenUtil.generateToken(user);
        String redirectionUrl = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("auth_token", token)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectionUrl);
    }

}