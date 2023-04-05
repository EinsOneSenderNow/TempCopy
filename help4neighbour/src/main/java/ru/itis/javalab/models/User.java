package ru.itis.javalab.models;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "users")
public class User {
    public enum State {
        DELETED,
        CONFIRMED,
        NOT_CONFIRMED,
        BANNED
    }

    public enum Role {
        USER, ADMIN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
    private String email;
    private String hashPassword;

    @Column(name = "card_id")
    private Long cardId;

    @Enumerated(value = EnumType.STRING)
    private State state;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    public boolean isConfirmed() {
        return this.state.equals(State.CONFIRMED);
    }

    public boolean isBanned() {
        return this.state.equals(State.BANNED);
    }


}
