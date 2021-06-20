package be.winagent.weba2.services.implementation;

import be.winagent.weba2.domain.models.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;


public class WebaUserDetails extends org.springframework.security.core.userdetails.User {
    private final User user;
    private static final String noPassword = "We don't do passwords here";

    public WebaUserDetails() {
        this("Anonymous");
    }

    public WebaUserDetails(String username) {
        super(username, noPassword, new ArrayList<>());
        user = null;
    }

    public WebaUserDetails(User user, Collection<GrantedAuthority> authorities) {
        super(
                user.getEmail(),
                noPassword,
                authorities
        );
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
