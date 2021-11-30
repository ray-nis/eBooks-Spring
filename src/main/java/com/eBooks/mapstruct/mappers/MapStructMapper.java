package com.eBooks.mapstruct.mappers;

import com.eBooks.authors.Author;
import com.eBooks.books.Book;
import com.eBooks.books.dto.BookPostDto;
import com.eBooks.genres.Genre;
import com.eBooks.genres.dto.GenreSlimDto;
import com.eBooks.mapstruct.dtos.AuthorSlimDto;
import com.eBooks.books.dto.BookGetDto;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
    BookGetDto bookToBookGetDto(Book book);
    AuthorSlimDto authorToAuthorSlimDto(Author author);
    GenreSlimDto genreToGenreSlimDto(Genre genre);

    @BeforeMapping
    default void setBookAuthor(@MappingTarget AuthorSlimDto authorSlimDto, Author author) {
        authorSlimDto.setFullName(author.getName() + " " + author.getLastName());
    }
}
