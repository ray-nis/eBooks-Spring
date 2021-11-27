package com.eBooks.books.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Setter
@Getter
public class BookPostDto {
    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("isbn")
    private String isbn;

    @JsonProperty("totalPages")
    private int totalPages;

    @JsonProperty("publishedDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    public Date publishedDate;

    @JsonProperty("authorsId")
    private Set<Long> authorsId;
}
