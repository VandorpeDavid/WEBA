package be.winagent.weba2.services.implementation;

import be.winagent.weba2.domain.models.User;
import be.winagent.weba2.domain.repositories.UserRepository;
import be.winagent.weba2.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;

    public User updateOrCreateUser(User user) {
        return findByUsername(user.getUsername())
                .map((existingUser) -> {
                    existingUser.setEmail(user.getEmail());
                    existingUser.setFirstName(user.getFirstName());
                    existingUser.setLastName(user.getLastName());

                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> userRepository.save(user));
    }

    @Override
    public Optional<User> find(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> allAdmins() {
        return userRepository.findAllByAdminTrue();
    }

    @Override
    public User makeAdmin(User user) {
        user.setAdmin(true);
        return userRepository.save(user);
    }

    @Override
    public User removeAdmin(User user) {
        user.setAdmin(false);
        return userRepository.save(user);
    }
}
