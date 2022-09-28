package ru.mirea.megatracker.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.mirea.megatracker.models.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUpUserDTO {
    @Email(message = "Email should be valid!")
    private String email;

    @Size(min = 4, message = "Password must contain more than 4 characters!")
    @NotEmpty(message = "Password cannot be empty!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotEmpty(message = "Please repeat your password!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String repeatedPassword;

    public SignUpUserDTO() {

    }


    public SignUpUserDTO(String email, String password, String repeatedPassword) {
        this.email = email;
        this.password = password;
        this.repeatedPassword = repeatedPassword;
    }

    public User toUser() {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
