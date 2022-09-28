package ru.mirea.megatracker.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignInUserDTO {
    @Email(message = "Email should be valid!")
    @NotEmpty(message = "Email can not be empty!")
    private String email;
    @Size(min = 4, message = "Password must contain more than 4 characters!")
    @NotEmpty(message = "Password can not be empty!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public SignInUserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
