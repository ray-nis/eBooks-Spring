package com.eBooks.genres;

import com.eBooks.exceptions.GenreNotFoundException;
import com.eBooks.genres.dto.GenrePostDto;
import com.eBooks.genres.dto.GenreSlimDto;
import com.eBooks.mapstruct.mappers.MapStructMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final MapStructMapper mapStructMapper;

    public Genre create(String name) {
        return genreRepository.save(
                Genre
                        .builder()
                        .name( name)
                        .books(new HashSet<>())
                        .build()
        );
    }

    public Genre findById(Long genreId) throws GenreNotFoundException {
        return genreRepository.findById(genreId).orElseThrow(GenreNotFoundException::new);
    }

    public List<GenreSlimDto> getGenres() {
        List<Genre> genres = genreRepository.findAll();
        return genres.stream().map(genre -> mapStructMapper.genreToGenreSlimDto(genre)).collect(Collectors.toList());
    }

    public void delete(Long id) throws GenreNotFoundException {
        genreRepository.delete(findById(id));
    }

    @Transactional
    public GenreSlimDto update(Long id, GenrePostDto genrePostDto) throws GenreNotFoundException {
        Genre genre = findById(id);
        genre.setName(genrePostDto.getName());
        return mapStructMapper.genreToGenreSlimDto(genreRepository.save(genre));
    }
}
