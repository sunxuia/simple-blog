package net.sunxu.demo.sb.service;

import java.util.Set;

public interface RoleService {
    String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    Set<String> findResources(String... roleName);

}