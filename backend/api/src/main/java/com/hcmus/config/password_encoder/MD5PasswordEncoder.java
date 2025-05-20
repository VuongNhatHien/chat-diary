package com.hcmus.config.password_encoder;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.hcmus.hashing.MD5;

public class MD5PasswordEncoder implements PasswordEncoder {
	@Override
	public String encode(CharSequence rawPassword) {
		String rawPasswordStr = rawPassword.toString();
		return MD5.hash(rawPasswordStr);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String rawPasswordStr = rawPassword.toString();
		return MD5.isMatch(rawPasswordStr, encodedPassword);
	}
}
