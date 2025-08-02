package com.laptopshop.laptopshop.service.userinfo;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.laptopshop.laptopshop.domain.Role;
import com.laptopshop.laptopshop.domain.User;
import com.laptopshop.laptopshop.service.UserService;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    public CustomOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // call api
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        // get provider
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // Process oAuth2User or map it to your local user database
        String email = (String) attributes.get("email");
        String fullName = (String) attributes.get("name");

        Role userRole = this.userService.fetchRoleByName("USER").get();

        if (email == null) {
            OAuth2Error error = new OAuth2Error("invalid_request",
                    "Can't get email address. Maybe login with private email (Github)", null);
            throw new OAuth2AuthenticationException(error);
        }

        if (email != null) {
            User user = this.userService.fetchUserByEmail(email);
            if (user == null) {
                User oUser = new User();
                oUser.setEmail(email);
                oUser.setAvatar(
                        registrationId.equalsIgnoreCase("github") ? "default-github.png" : "default-google.png");
                oUser.setFullName(fullName);
                oUser.setProvider(registrationId.equalsIgnoreCase("github") ? "GITHUB" : "GOOGLE");
                oUser.setPassword("empty_oauth2");
                oUser.setRole(userRole);

                this.userService.saveUser(oUser);

            }
        }

        return new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userRole.getName())),
                oAuth2User.getAttributes(), "email");
    }
}