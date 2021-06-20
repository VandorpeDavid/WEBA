package be.winagent.weba2.services;

import be.winagent.weba2.domain.models.User;
import org.springframework.security.core.Authentication;
import java.util.Optional;

public interface AuthenticationService {
    Optional<User> getUser(Authentication authentication);
}
