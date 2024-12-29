package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="roles")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="role_name", unique = true, nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "dentist_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "dentist_id")
    )
    @ToString.Exclude
    @JsonIgnore
    private List<Dentist> dentists;

    public Role(String name) {
        this.name = name;
    }
}
