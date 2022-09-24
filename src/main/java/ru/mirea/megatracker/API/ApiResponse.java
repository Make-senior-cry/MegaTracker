package ru.mirea.megatracker.API;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse{

    @JsonProperty(value = "Data")
    private List<Coin> data = new ArrayList<Coin>();

    @JsonProperty(value = "Message")
    private String message;

}