package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(name = "dentists")
public class Dentist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "fees")
    private String fees;

    @Column(name = "speciality")
    private String speciality;

    @Column(name = "status")
    private int status;

    @Column(name = "is_working")
    private int isWorking;

    @Column(name = "about")
    private String about;

    @Column(name = "position")
    private String position;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "dentist_role",
            joinColumns = @JoinColumn(name = "dentist_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    private List<Role> roles;

    public Dentist(String imgUrl, String name, String email, String password, String fees, String speciality, String position, int status, int isWorking, String about, List<Role> roles) {
        this.imgUrl = imgUrl;
        this.name = name;
        this.email = email;
        this.password = password;
        this.fees = fees;
        this.speciality = speciality;
        this.status = status;
        this.isWorking = isWorking;
        this.about = about;
        this.position = position;
        this.roles = roles;
    }

}
