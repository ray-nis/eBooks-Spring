package com.eBooks.genres.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenrePostDto {
    @JsonProperty("name")
    private String name;
}
