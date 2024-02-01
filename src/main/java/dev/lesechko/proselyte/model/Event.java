package dev.lesechko.proselyte.model;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

import dev.lesechko.proselyte.utils.CustomUserSerializer;


@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Getter @Setter
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonSerialize(using = CustomUserSerializer.class)
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "file_id")
    private File file;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public Event() {}

    public Event(User user, File file) {
        this.user = user;
        this.file = file;
        this.status = Status.ACTIVE;
    }
}
