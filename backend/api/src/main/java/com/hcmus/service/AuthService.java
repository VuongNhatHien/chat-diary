package com.hcmus.service;

import com.hcmus.config.ChatDiaryUserDetails;
import com.hcmus.dto.request.LoginRequest;
import com.hcmus.dto.request.RegisterRequest;
import com.hcmus.dto.response.ErrorCodes;
import com.hcmus.exception.BadRequestException;
import com.hcmus.model.Role;
import com.hcmus.model.User;
import com.hcmus.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.hcmus.constant.GeneralConstant.ACCESS_TOKEN_KEY;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    public void signup(RegisterRequest input) {
        User user = modelMapper.map(input, User.class);

        user.setRoles(Role.USER.getValue());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new BadRequestException(ErrorCodes.EMAIL_DUPLICATED);
        }
    }

    public ChatDiaryUserDetails login(LoginRequest input) {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new BadRequestException(ErrorCodes.EMAIL_NOT_FOUND));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getId(), input.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadRequestException(ErrorCodes.WRONG_PASSWORD);
        }

        return new ChatDiaryUserDetails(user);
    }

    public String getMeId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public User getMe() {
        String meId = getMeId();
        return userRepository.findById(meId).orElse(null);
    }

    public void setCookie(HttpServletResponse servletResponse, String accessToken) {
        Cookie cookie = new Cookie(ACCESS_TOKEN_KEY, accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "None");

        servletResponse.addCookie(cookie);
    }

    public void removeCookie(HttpServletResponse servletResponse) {
        Cookie cookie = new Cookie(ACCESS_TOKEN_KEY, "");

        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "None");

        servletResponse.addCookie(cookie);
    }
}
