package com.eBooks.books.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class BookSlimDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("publishedDate")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "UTC")
    private Instant publishedDate;
}
