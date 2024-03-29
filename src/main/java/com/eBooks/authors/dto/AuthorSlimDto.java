package com.eBooks.authors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorSlimDto {
    @JsonProperty("id")
    private Long id;

    private String name;

    private String lastName;

    private String fullName;
}
