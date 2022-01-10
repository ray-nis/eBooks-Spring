package com.eBooks.authors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorPostDto {
    @JsonProperty("name")
    private String name;

    @JsonProperty("lastName")
    private String lastName;
}
