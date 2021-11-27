package com.eBooks.mapstruct.mappers;

import com.eBooks.authors.Author;
import com.eBooks.books.Book;
import com.eBooks.mapstruct.dtos.AuthorSlimDto;
import com.eBooks.books.dto.BookGetDto;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
    BookGetDto bookToBookGetDto(Book book);
    AuthorSlimDto authorToAuthorSlimDto(Author author);

    @BeforeMapping
    default void setBookAuthor(@MappingTarget AuthorSlimDto authorSlimDto, Author author) {
        authorSlimDto.setFullName(author.getName() + " " + author.getLastName());
    }
}
