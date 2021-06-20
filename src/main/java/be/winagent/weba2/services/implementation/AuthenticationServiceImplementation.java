package be.winagent.weba2.services.implementation;

import be.winagent.weba2.domain.models.User;
import be.winagent.weba2.services.AuthenticationService;
import be.winagent.weba2.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationServiceImplementation implements AuthenticationService {
    private final UserService userService;
    @Override
    public Optional<User> getUser(Authentication authentication) {
        return Optional.ofNullable(authentication)
                .map(Authentication::getPrincipal)
                .filter((x) -> x instanceof WebaUserDetails)
                .map((x) -> (WebaUserDetails) x)
                .map(WebaUserDetails::getUser)
                .map(User::getId)
                .flatMap(userService::find); // Update with latest data
    }
}
