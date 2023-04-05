package ru.itis.javalab.models;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String header;

    private String description;

    private Integer price;

    private String address;

    @Enumerated(value = EnumType.STRING)
    private State state;

    public enum State {
        ACTIVE,
        ACCEPTED,
        DRAFT,
        BLOCKED,
        DELETED
    }
}
