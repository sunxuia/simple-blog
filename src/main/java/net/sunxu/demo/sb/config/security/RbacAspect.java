package net.sunxu.demo.sb.config.security;

import net.sunxu.demo.sb.service.RoleService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static net.sunxu.demo.sb.util.SbWebUtils.*;

/**
 * 进行权限判断的拦截类
 */
@Aspect
@Component
public class RbacAspect {

    private Logger logger = LoggerFactory.getLogger(RbacAspect.class);

    @Autowired
    private RoleService roleService;

    @Before("(@within(net.sunxu.demo.sb.config.security.Resource) " +
            "|| @annotation(net.sunxu.demo.sb.config.security.Resource)) && target(target)")
    private void accessAuthorization(JoinPoint point, Object target) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Class<?> targetClass = targetMethod.getDeclaringClass();

        LocalUserDetails principal = (LocalUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("user [{}][{}] access resource [{}#{}]",
                principal.getUsername(), getIpAddress(getRequest()), targetClass, targetMethod.getName());

        String[] roleNames = principal.getRoleNames().toArray(new String[principal.getRoleNames().size()]);
        Set<String> availableResources = roleService.findResources(roleNames);

        Resource[] classResources = targetClass.getAnnotationsByType(Resource.class);
        Resource[] methodResources = targetMethod.getAnnotationsByType(Resource.class);
        boolean hasAuthorization = false;
        if (methodResources.length == 0) {
            for (int i = 0; i < classResources.length && !hasAuthorization; i++) {
                hasAuthorization = isPathMatch(availableResources, classResources[i].value());
            }
        } else if (classResources.length == 0) {
            for (int i = 0; i < methodResources.length && !hasAuthorization; i++) {
                hasAuthorization = isPathMatch(availableResources, methodResources[i].value());
            }
        } else {
            for (int i = 0; i < classResources.length && !hasAuthorization; i++) {
                for (int j = 0; j < methodResources.length && !hasAuthorization; i++) {
                    hasAuthorization = isPathMatch(availableResources,
                            classResources[i].value() + "." + methodResources[i].value());
                }
            }
        }
        if (!hasAuthorization) {
            throw new AccessDeniedException("NOT_AUTHORIZED");
        }
    }

    private boolean isPathMatch(Set<String> availableResources, String destResource) {
        AntPathMatcher matcher = new AntPathMatcher(".");
        for (String availableResource : availableResources) {
            if (matcher.match(availableResource, destResource)) {
                return true;
            }
        }
        return false;
    }
}
