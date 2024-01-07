package ru.vedeshkin.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.vedeshkin.project.entity.Book;
import ru.vedeshkin.project.model.CustomUserDetails;
import ru.vedeshkin.project.repository.BookRepository;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping()
    public ModelAndView getAllBooks(@AuthenticationPrincipal CustomUserDetails user) {
        ModelAndView mav = new ModelAndView("list-books");
        mav.addObject("user", user);
        mav.addObject("books", bookRepository.findAll());
        return mav;
    }

    @GetMapping("/add")
    public ModelAndView addBookForm(@AuthenticationPrincipal CustomUserDetails user) {
        ModelAndView mav = new ModelAndView("book-form");
        mav.addObject("user", user);
        mav.addObject("book", new Book());
        return mav;
    }

    @PostMapping()
    public String saveBook(@ModelAttribute Book book) {
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/edit")
    public ModelAndView editBookForm(@AuthenticationPrincipal CustomUserDetails user,
                                     @RequestParam Long bookId) {
        ModelAndView mav = new ModelAndView("book-form");

        Optional<Book> optionalBook = bookRepository.findById(bookId);
        Book book = new Book();
        if (optionalBook.isPresent()) {
            book = optionalBook.get();
        }
        mav.addObject("user", user);
        mav.addObject("book", book);
        return mav;
    }

    @GetMapping("/delete")
    public String deleteBook(@RequestParam Long bookId) {
        bookRepository.deleteById(bookId);
        return "redirect:/books";
    }

}
