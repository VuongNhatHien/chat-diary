package com.hcmus.config;

import com.hcmus.dto.response.ErrorCodes;
import com.hcmus.exception.NotFoundException;
import com.hcmus.model.User;
import com.hcmus.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChatDiaryUserDetailService implements UserDetailsService {

    private final UserRepository userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail = userService.findById(username);

        return userDetail.map(ChatDiaryUserDetails::new)
            .orElseThrow(() -> new NotFoundException(ErrorCodes.USER_NOT_FOUND));

    }
}
