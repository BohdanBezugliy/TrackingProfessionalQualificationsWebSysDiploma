package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String rootRedirect(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    return "redirect:/admin/dashboard";
                }
                if (authority.getAuthority().equals("ROLE_USER")) {
                    return "redirect:/user/dashboard";
                }
            }
        }
        return "redirect:/login";
    }
}
