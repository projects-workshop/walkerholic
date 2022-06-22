package com.yunhalee.walkerholic.security.jwt.service;


import com.yunhalee.walkerholic.security.jwt.JwtTokenUtil;
import com.yunhalee.walkerholic.security.jwt.domain.JwtUserDetails;
import com.yunhalee.walkerholic.security.jwt.dto.JwtRequest;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.user.dto.UserResponse;
import com.yunhalee.walkerholic.user.dto.UserTokenResponse;
import com.yunhalee.walkerholic.user.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class JwtUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private JwtTokenUtil jwtTokenUtil;


    public JwtUserDetailsService(UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByEmail(username);
        return new JwtUserDetails(user);
    }

    public UserTokenResponse signIn(JwtRequest request) {
        final UserDetails userDetails = loadUserByUsername(request.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails.getUsername());
        User user = findUserByEmail(userDetails.getUsername());
        return UserTokenResponse.of(UserResponse.of(user), token);
    }

    public UserTokenResponse signInWithToken(String token) {
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = findUserByEmail(email);
        return UserTokenResponse.of(UserResponse.of(user), token);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByUserAuthEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));
    }


}
