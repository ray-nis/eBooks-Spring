package com.eBooks.genres.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreSlimDto {
    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;
}
