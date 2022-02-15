package com.eBooks.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class PaginationUtil {
    public static int getPage(Optional<Integer> page) {
        if (page.isPresent() && page.get() > 0) {
            return page.get() - 1;
        }
        return 0;
    }

    public static String getSort(Optional<String> sort) {
        if (sort.isPresent() && sort.get().toUpperCase().equals("DSC")) {
            return "DSC";
        }
        return "ASC";
    }

    public static Pageable getAllBooksPageable(Optional<Integer> page, Optional<String> sort) {
        int currentPage = getPage(page);
        String sortDir = getSort(sort);
        if (sortDir.equals("DSC")) {
            return PageRequest.of(currentPage, 10, Sort.by("title").descending());
        }
        else {
            return PageRequest.of(currentPage, 10, Sort.by("title").ascending());
        }
    }
}
