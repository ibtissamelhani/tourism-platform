package org.ibtissam.dadesadventures.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    TOURIST(Set.of(
            Permission.REGISTER,
            Permission.VIEW_PLACES_ACTIVITIES,
            Permission.LEAVE_REVIEW,
            Permission.BOOK_ACTIVITY
    )),
    GUIDE(Set.of(
            Permission.REGISTER,
            Permission.VIEW_PLACES_ACTIVITIES,
            Permission.CREATE_ACTIVITY,
            Permission.MODIFY_OWN_ACTIVITY,
            Permission.CANCEL_OWN_ACTIVITY
    )),
    ADMIN(Set.of(
            Permission.REGISTER,
            Permission.VIEW_PLACES_ACTIVITIES,
            Permission.MANAGE_ACTIVITIES,
            Permission.MANAGE_USERS,
            Permission.MANAGE_PLACES_CATEGORIES
    ));

    @Getter
    private final Set<Permission> permissions;


    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities =getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));
        return authorities;
    }
}
