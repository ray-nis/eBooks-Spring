package com.eBooks.mapstruct.mappers;

import com.eBooks.authors.Author;
import com.eBooks.books.Book;
import com.eBooks.mapstruct.dtos.AuthorSlimDto;
import com.eBooks.mapstruct.dtos.BookGetDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
    BookGetDto bookToBookGetDto(Book book);
    AuthorSlimDto authorToAuthorSlimDto(Author author);
}
