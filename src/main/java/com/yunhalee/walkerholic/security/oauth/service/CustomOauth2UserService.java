package com.yunhalee.walkerholic.security.oauth.service;

import com.yunhalee.walkerholic.security.jwt.JwtTokenUtil;
import com.yunhalee.walkerholic.security.jwt.domain.JwtUserDetails;
import com.yunhalee.walkerholic.user.domain.Level;
import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.security.oauth.exception.OAuthProviderMissMatchException;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.security.oauth.domain.OAuth2UserInfo;
import com.yunhalee.walkerholic.security.oauth.domain.OAuth2UserInfoFactory;
import com.yunhalee.walkerholic.security.oauth.domain.ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("attributes" + super.loadUser(userRequest).getAttributes());
        OAuth2User user = super.loadUser(userRequest);

        try {
            return process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw new OAuth2AuthenticationException(ex.getMessage());
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType
            .valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory
            .getOAuth2UserInfo(providerType, user.getAttributes());

        User savedUser = userRepository.findByEmail(userInfo.getEmail());

        if (savedUser != null) {
            if (providerType != savedUser.getProviderType()) {
                throw new OAuthProviderMissMatchException(
                    "Looks like you're signed up with " + providerType +
                        " account. Please use your " + savedUser.getProviderType()
                        + " account to login."
                );
            }
            updateUser(savedUser, userInfo);
        } else {
            savedUser = createUser(userInfo, providerType);
        }
        System.out.println(jwtTokenUtil.generateToken(userInfo.getEmail()));

        return new JwtUserDetails(savedUser, user.getAttributes());
    }

    private User createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        User user = new User();
        user.setFirstname(userInfo.getFirstName());
        user.setLastname(userInfo.getLastName());
        user.setRole(Role.USER);
        user.setLevel(Level.Starter);
        user.setPassword("");
        user.setEmail(userInfo.getEmail());
        user.setImageUrl(userInfo.getImageUrl());
        user.setProviderType(providerType);

        return userRepository.save(user);
    }

    private User updateUser(User user, OAuth2UserInfo userInfo) {
        if (userInfo.getFirstName() != null && !user.getFullname().equals(userInfo.getName())) {
            user.setFirstname(userInfo.getFirstName());
        }

        if (userInfo.getLastName() != null && !user.getLastname().equals(userInfo.getLastName())) {
            user.setLastname(userInfo.getLastName());
        }

        if (userInfo.getImageUrl() != null && !user.getImageUrl().equals(userInfo.getImageUrl())) {
            user.setImageUrl(userInfo.getImageUrl());
        }

        return user;
    }
}
