package com.eBooks.books;

import com.eBooks.genres.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    //List<Book> findAllByGenresIn(HashSet<Genre> genres);
}
