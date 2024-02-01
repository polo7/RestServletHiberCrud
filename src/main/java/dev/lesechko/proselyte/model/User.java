package dev.lesechko.proselyte.model;

import java.util.List;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Getter @Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Event> events;

    public User() {}

    public User(String name) {
        this.name = name;
        this.status = Status.ACTIVE;
        this.events = null;
    }
}
