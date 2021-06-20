package be.winagent.weba2.services;

import be.winagent.weba2.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User updateOrCreateUser(User user);
    Optional<User> find(Long id);
    Optional<User> findByUsername(String username);
    List<User> allAdmins();
    User makeAdmin(User user);
    User removeAdmin(User user);

}
