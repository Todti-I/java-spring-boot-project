package ru.vedeshkin.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vedeshkin.project.dto.BookDto;
import ru.vedeshkin.project.entity.Book;
import ru.vedeshkin.project.entity.User;
import ru.vedeshkin.project.repository.BookRepository;
import ru.vedeshkin.project.repository.ShopRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ShopRepository shopRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, ShopRepository shopRepository) {
        this.bookRepository = bookRepository;
        this.shopRepository = shopRepository;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book save(BookDto bookDto, User currentUser) {
        Book book = new Book();
        if (bookDto.getId() != null) {
            Optional<Book> optionalBook = bookRepository.findById(bookDto.getId());
            if (optionalBook.isPresent()) {
                book = optionalBook.get();
            }
        } else {
            book.setOwnerUser(currentUser);
        }
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPrice(bookDto.getPrice());
        book.setShops(shopRepository.findAllById(bookDto.getShopIds()));
        return bookRepository.save(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

}
