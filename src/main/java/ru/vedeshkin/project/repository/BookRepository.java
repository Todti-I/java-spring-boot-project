package ru.vedeshkin.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vedeshkin.project.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
