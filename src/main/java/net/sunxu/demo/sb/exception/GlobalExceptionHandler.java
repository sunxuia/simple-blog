package net.sunxu.demo.sb.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ModelAndView accessDeniedExceptionHandler(HttpServletRequest req, HttpServletResponse resp,
                                                     AccessDeniedException err) {
        resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ModelAndView res = new ModelAndView("exception/403");
        res.addObject("referer", req.getHeader("referer"));
        return res;
    }


}
