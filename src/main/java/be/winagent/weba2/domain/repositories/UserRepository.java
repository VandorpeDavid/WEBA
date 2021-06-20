package be.winagent.weba2.domain.repositories;

import be.winagent.weba2.domain.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findAllByAdminTrue();
}
