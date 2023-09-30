package com.example.api.Security;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Yassine Deriouch
 * @project API
 */
public class CustomPasswordEnconder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return charSequence.toString().equals(s);
    }
}
