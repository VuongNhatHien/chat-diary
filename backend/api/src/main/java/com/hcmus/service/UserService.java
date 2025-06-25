package com.hcmus.service;

import com.hcmus.exception.NotFoundException;
import com.hcmus.model.User;
import com.hcmus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void updateUserAvatar(String userId, String avatarUrl) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
    }
}
