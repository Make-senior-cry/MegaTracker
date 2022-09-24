package ru.mirea.megatracker.api;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoinInfo {
    @JsonProperty(value = "Name")
    private String name;

    @JsonProperty(value = "FullName")
    private String fullName;

    @JsonProperty(value = "ImageUrl")
    private String imageUrl;
}
