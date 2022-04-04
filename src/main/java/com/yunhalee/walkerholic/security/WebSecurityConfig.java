package com.yunhalee.walkerholic.security;


import com.yunhalee.walkerholic.security.jwt.JwtAuthenticationEntryPoint;
import com.yunhalee.walkerholic.security.jwt.JwtRequestFilter;
import com.yunhalee.walkerholic.security.jwt.service.JwtUserDetailsService;
import com.yunhalee.walkerholic.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.yunhalee.walkerholic.security.oauth.OAuth2AuthenticationFailureHandler;
import com.yunhalee.walkerholic.security.oauth.OAuth2SuccessHandler;
import com.yunhalee.walkerholic.security.oauth.service.CustomOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomOauth2UserService customOauth2UserService;

    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Autowired
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(jwtUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors()
            .and()
            .csrf()
            .disable()
            .formLogin()
            .disable()
            .authorizeRequests()
            .antMatchers("/**","/authenticate", "/signin", "/signup", "/user/check_email",
                "/user/register", "/activities", "/activity/**", "/posts",
                "/posts/search", "/categories", "/levels", "/user/forgotPassword/**",
                "/product/**", "/products/**", "/user/search/**", "/posts/search/**", "/users/**")
            .permitAll()
            .antMatchers("/activity/save", "/deleteActivity/**", "/orderlist/**", "/productlist/**",
                "/userlist/**", "/user/delete/**").hasAnyAuthority("ADMIN")
            .antMatchers("/orderlistBySeller/**", "/order/deliver/**", "/users/**/**",
                "/product/save").hasAnyAuthority("ADMIN", "SELLER")
            .antMatchers("/userActivities/**", "/userActivity/**", "/follows/**", "/unfollow/**",
                "/cartItems/**", "/createCart/**", "/addToCart/**", "/updateQty/**",
                "/deleteOrderItem/**", "/orderlistByUser/**", "/payOrder", "/payOrder/**",
                "/getOrder/**", "/order/cancel/**").hasAnyAuthority("ADMIN", "SELLER", "USER")
            .anyRequest()
            .authenticated()
//                    .permitAll()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .oauth2Login()
            .authorizationEndpoint()
            .authorizationRequestRepository(cookieAuthorizationRequestRepository())
            .baseUri("/oauth2/authorization")
            .and()
            .redirectionEndpoint()
            .baseUri("/oauth2/callback/*")
            .and()
            .userInfoEndpoint()
            .userService(customOauth2UserService)
            .and()
            .successHandler(oAuth2SuccessHandler)
            .failureHandler((AuthenticationFailureHandler) oAuth2AuthenticationFailureHandler);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/profileUploads/**", "/messageUploads/**", "/productUploads/**",
            "/activityUploads/**", "/postUploads/**", "/js/**", "/webjars/**");
    }
}