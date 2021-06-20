package be.winagent.weba2.services;

import be.winagent.weba2.domain.models.Association;
import be.winagent.weba2.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface AssociationService {
    List<Association> all();
    Optional<Association> find(long id);
    Optional<Association> findByAbbreviation(String abbreviation);
    Association create(Association association);
    Association update(Association association);
    User addAdmin(Association association, User user);
    Association removeAdmin(Association association, String username);
}
