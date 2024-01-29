package dev.lesechko.proselyte.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "users")
public class User {
    @Getter @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Getter @Setter
    @Column(name = "name")
    private String name;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Getter @Setter
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Event> events;

    public User() {}

    public User(String name) {
        this.name = name;
        this.status = Status.ACTIVE;
        this.events = null;
    }
}
