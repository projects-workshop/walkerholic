package com.yunhalee.walkerholic.security.jwt.controller;


import com.yunhalee.walkerholic.common.exception.AuthException;
import com.yunhalee.walkerholic.security.jwt.dto.JwtRequest;
import com.yunhalee.walkerholic.security.jwt.service.JwtUserDetailsService;
import com.yunhalee.walkerholic.user.dto.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;


    private final JwtUserDetailsService userDetailsService;


    @PostMapping("/api/sign-in")
    public ResponseEntity<UserTokenResponse> signIn(@RequestBody JwtRequest request) {
        authenticate(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(userDetailsService.signIn(request));
    }


    private void authenticate(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException e) {
            throw new AuthException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new AuthException("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping(value = "/api/sign-in", params = "token")
    public ResponseEntity<UserTokenResponse> authenticate(@RequestParam("token") String token) {
        return ResponseEntity.ok(userDetailsService.signInWithToken(token));
    }


}