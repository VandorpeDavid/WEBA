package be.winagent.weba2.services.implementation;

import be.winagent.weba2.domain.models.Association;
import be.winagent.weba2.domain.models.User;
import be.winagent.weba2.domain.repositories.AssociationRepository;
import be.winagent.weba2.services.AssociationService;
import be.winagent.weba2.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AssociationServiceImplementation implements AssociationService {
    private final AssociationRepository associationRepository;
    private final UserService userService;

    @Override
    public List<Association> all() {
        return associationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public Optional<Association> find(long id) {
        return associationRepository.findById(id);
    }

    @Override
    public Optional<Association> findByAbbreviation(String abbreviation) {
        return associationRepository.findByAbbreviation(abbreviation);
    }

    @Override
    public Association create(Association association) {
        return associationRepository.save(association);
    }

    @Override
    public Association update(Association association) {
        return associationRepository.save(association);
    }

    @Override
    public User addAdmin(Association association, User user) {
        association.getAdmins().add(user);
        associationRepository.save(association);
        return user;
    }

    @Override
    public Association removeAdmin(Association association, String username) {
        boolean changed = association.getAdmins().removeIf((admin) -> admin.getUsername().equals(username));
        if (changed) {
            return associationRepository.save(association);
        }
        return association;
    }
}
