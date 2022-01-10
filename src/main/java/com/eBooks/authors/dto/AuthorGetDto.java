package com.eBooks.authors.dto;

import com.eBooks.books.dto.BookSlimDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AuthorGetDto {
    @JsonProperty("id")
    private Long id;

    private String name;

    private String lastName;

    private String fullName;

    private List<BookSlimDto> books;
}
