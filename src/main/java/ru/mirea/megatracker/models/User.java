package ru.mirea.megatracker.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email")
    @Email(message = "Email is invalid!")
    @NotEmpty(message = "Email cannot be empty!")
    private String email;

    @Column(name = "password")
    @Size(min = 4, message = "Password must contain more than 4 characters!")
    @NotEmpty(message = "Password cannot be empty!")
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Note> note;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
