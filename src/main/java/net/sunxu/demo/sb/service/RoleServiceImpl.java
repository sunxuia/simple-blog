package net.sunxu.demo.sb.service;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    @Override
    public Set<String> findResources(String... roleName) {
        return Set.of("*");
    }
}
