package com.hcmus.hashing.md5;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Base64;

public class MD5PasswordEncoder implements PasswordEncoder {
	private static final int SALT_LENGTH = 16;

	private String generateSalt() {
		byte[] salt = new byte[SALT_LENGTH];
		new SecureRandom().nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	@Override
	public String encode(CharSequence rawPassword) {
		String salt = generateSalt();
		String saltedPassword = salt + rawPassword;
		String hashed = MD5.hash(saltedPassword);
		return salt + ":" + hashed;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String[] parts = encodedPassword.split(":");
		if (parts.length != 2) return false;

		String salt = parts[0];
		String storedHash = parts[1];
		String saltedPassword = salt + rawPassword;
		String computedHash = MD5.hash(saltedPassword);
		return storedHash.equals(computedHash);
	}
}
