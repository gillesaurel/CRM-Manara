package com.manara.portail.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class CookieInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {

        if (modelAndView == null) return;

        String role = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("role".equals(cookie.getName())) {
                    role = cookie.getValue();
                    break;
                }
            }
        }

        modelAndView.addObject("cookieValue", role);
    }
}