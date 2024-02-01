package dev.lesechko.proselyte.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "file_path")
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public File() {}

    public File(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
        this.status = Status.ACTIVE;
    }

    public File(Integer id, String name, String filePath) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
        this.status = Status.ACTIVE;
    }
}
