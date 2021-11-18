package com.battleface.challengeDemo.config;

import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {

    private static final String BEARER_TOKEN_TYPE = "Bearer";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String ERROR_STATUS = "error_status";
    private static final String CONTENT_HEADER = "Content-Type";

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        String contentHeader = request.getHeader(CONTENT_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_TOKEN_TYPE)) {
            request.setAttribute(ERROR_STATUS, response);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token type");
            return false;
        }

        if (contentHeader == null || !contentHeader.trim().equalsIgnoreCase("application/json")) {
            request.setAttribute(ERROR_STATUS, response);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid content type");
            return false;
        }
        // should be a space after token type
        if (authHeader != null && authHeader.length() < 8) {
            request.setAttribute(ERROR_STATUS, response);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid token length");
            return false;
        }

        return true;
    }
}
