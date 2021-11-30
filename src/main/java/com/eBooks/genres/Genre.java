package com.eBooks.genres;

import com.eBooks.books.Book;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Genre is required.")
    private String name;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    private Set<Book> books;
}
