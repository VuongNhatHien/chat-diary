package com.hcmus.chatdiary.config;

import com.hcmus.chatdiary.dto.response.ErrorCodes;
import com.hcmus.chatdiary.exception.NotFoundException;
import com.hcmus.chatdiary.model.User;
import com.hcmus.chatdiary.repository.UserRepository;
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
