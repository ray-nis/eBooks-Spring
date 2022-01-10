package com.eBooks.mapstruct.mappers;

import com.eBooks.authors.Author;
import com.eBooks.authors.dto.AuthorGetDto;
import com.eBooks.books.Book;
import com.eBooks.books.dto.BookSlimDto;
import com.eBooks.genres.Genre;
import com.eBooks.genres.dto.GenreSlimDto;
import com.eBooks.authors.dto.AuthorSlimDto;
import com.eBooks.books.dto.BookGetDto;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
    BookGetDto bookToBookGetDto(Book book);
    AuthorSlimDto authorToAuthorSlimDto(Author author);
    GenreSlimDto genreToGenreSlimDto(Genre genre);
    AuthorGetDto authorToAuthorGetDto(Author author);
    BookSlimDto bookToBookSlimDto(Book book);
}
