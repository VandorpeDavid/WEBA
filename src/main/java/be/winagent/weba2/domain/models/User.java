package be.winagent.weba2.domain.models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@javax.persistence.Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    @NotEmpty
    private String username;

    private boolean admin;

    @Email
    private String email;

    private String firstName;
    private String lastName;

    public void setEmail(String email) {
        this.email = email.strip();
    }

    public String toString() {
        return String.format("%s (%s)", username, getEmail());
    }
}
