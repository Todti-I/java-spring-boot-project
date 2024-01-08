package ru.vedeshkin.project.service;

import ru.vedeshkin.project.dto.BookDto;
import ru.vedeshkin.project.entity.Book;
import ru.vedeshkin.project.entity.User;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<Book> findById(Long id);

    List<Book> findAll();

    Book save(BookDto bookDto, User currentUser);

    void deleteById(Long id);

}
