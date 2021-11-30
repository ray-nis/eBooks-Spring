package com.eBooks.books.dto;

import com.eBooks.genres.Genre;
import com.eBooks.genres.dto.GenreSlimDto;
import com.eBooks.mapstruct.dtos.AuthorSlimDto;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonProperty("description")
    private String description;

    @JsonProperty("isbn")
    private String isbn;

    @JsonProperty("totalPages")
    private int totalPages;

    @JsonProperty("publishedDate")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "UTC")
    private Instant publishedDate;

    @JsonProperty("authors")
    private Set<AuthorSlimDto> authors;

    @JsonProperty("genres")
    private Set<GenreSlimDto> genres;
}
