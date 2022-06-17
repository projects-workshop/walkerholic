package com.yunhalee.walkerholic.security.oauth.service;

import com.yunhalee.walkerholic.common.exception.AuthException;
import com.yunhalee.walkerholic.security.jwt.JwtTokenUtil;
import com.yunhalee.walkerholic.security.jwt.domain.JwtUserDetails;
import com.yunhalee.walkerholic.security.oauth.domain.OAuth2UserInfo;
import com.yunhalee.walkerholic.security.oauth.domain.OAuth2UserInfoFactory;
import com.yunhalee.walkerholic.security.oauth.domain.ProviderType;
import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private UserRepository userRepository;
    private JwtTokenUtil jwtTokenUtil;

    public CustomOauth2UserService(UserRepository userService, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        log.info("attributes" + super.loadUser(userRequest).getAttributes());
        OAuth2User user = super.loadUser(userRequest);
        try {
            return process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw new AuthException(ex.getMessage());
        } catch (Exception ex) {
            throw new AuthException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType
            .valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory
            .getOAuth2UserInfo(providerType, user.getAttributes());
        Optional<User> optionalUser = userRepository.findByEmail(userInfo.getEmail());
        log.info(jwtTokenUtil.generateToken(userInfo.getEmail()));
        return new JwtUserDetails(user(optionalUser, userInfo, providerType), user.getAttributes());
    }

    private User user(Optional<User> user, OAuth2UserInfo userInfo, ProviderType providerType) {
        if (user.isEmpty()) {
            return createUser(userInfo, providerType);
        }
        User existingUser = user.get();
        if (providerType != existingUser.getProviderType()) {
            throw new AuthException("Looks like you're signed up with " + providerType + " account. Please use your " + existingUser.getProviderType() + " account to login.");
        }
        existingUser.updateOAuth2User(userInfo);
        return existingUser;
    }

    private User createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        User user = User.builder()
            .firstname(userInfo.getFirstName())
            .lastname(userInfo.getLastName())
            .role(Role.USER)
            .password("")
            .email(userInfo.getEmail())
            .imageUrl(userInfo.getImageUrl())
            .providerType(providerType).build();
        return userRepository.save(user);
    }
}
