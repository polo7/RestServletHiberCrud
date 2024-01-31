package dev.lesechko.proselyte.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;


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
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
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
