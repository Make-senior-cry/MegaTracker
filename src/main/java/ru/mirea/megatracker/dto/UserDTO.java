package ru.mirea.megatracker.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDTO {
    @Email(message = "Email should be valid!")
    private String email;
    @Size(min = 4, message = "Password must contain more than 4 characters!")
    @NotEmpty(message = "Password cannot be empty!")
    private String password;

    @Size(min = 4, message = "Password must contain more than 4 characters!")
    @NotEmpty(message = "Please repeat your password!")
    private String repeatedPassword;

    public UserDTO(String email, String password, String repeatedPassword) {
        this.email = email;
        this.password = password;
        this.repeatedPassword = repeatedPassword;
    }
}
