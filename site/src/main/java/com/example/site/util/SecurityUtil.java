package com.example.site.util;

import com.example.site.security.UserDetailImpl;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtil {

    public UserDetailImpl getUserDetail() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }

        return (UserDetailImpl) authentication.getPrincipal();
    }
}
