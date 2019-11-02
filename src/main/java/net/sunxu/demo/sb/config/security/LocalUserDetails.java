package net.sunxu.demo.sb.config.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.sunxu.demo.sb.entity.Role;
import net.sunxu.demo.sb.entity.User;
import net.sunxu.demo.sb.entity.UserState;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.security.SocialUserDetails;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@ToString
public class LocalUserDetails implements SocialUserDetails {

    private final User user;

    private final Set<SimpleGrantedAuthority> authorities;

    private final Set<String> roleNames;

    public LocalUserDetails(User user) {
        this.user = user;
        roleNames = user.getRoles().stream().map(Role::getName).collect(toSet());
        authorities = roleNames.stream().map(SimpleGrantedAuthority::new).collect(toSet());
    }

    @Override
    public String getUserId() {
        return user.getId().toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getUserState() != UserState.EXIPRED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getUserState() != UserState.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return user.getId();
    }

    public Set<String> getRoleNames() {
        return roleNames;
    }

    public String getAvatarUrl() {
        return user.getAvatarUrl();
    }

    public String getSelfDescription() {
        return user.getSelfIntroduction();
    }

    public User getUser() {
        return user;
    }

    @Getter
    @Setter
    private Integer notifyCount;

}
