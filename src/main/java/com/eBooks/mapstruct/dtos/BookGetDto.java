package com.eBooks.mapstruct.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
public class BookGetDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("isbn")
    private String isbn;

    @JsonProperty("publishedDate")
    private Instant publishedDate;

    @JsonProperty("authors")
    private Set<AuthorSlimDto> authors;
}
