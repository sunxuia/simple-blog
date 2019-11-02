package net.sunxu.demo.sb.config.security;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RefererSuccessHandler implements LogoutSuccessHandler {
    private final String defaultUrl;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public RefererSuccessHandler(String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        redirect(request, response);
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = request.getHeader("Referer");
        if (Strings.isEmpty(url)) {
            url = defaultUrl;
        }
        redirectStrategy.sendRedirect(request, response, url);
    }

}
