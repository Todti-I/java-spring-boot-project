package ru.vedeshkin.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.vedeshkin.project.dto.BookDto;
import ru.vedeshkin.project.entity.Book;
import ru.vedeshkin.project.model.CustomUserDetails;
import ru.vedeshkin.project.repository.BookRepository;
import ru.vedeshkin.project.repository.ShopRepository;

@Slf4j
@Controller
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;
    private final ShopRepository shopRepository;

    @Autowired
    public BookController(BookRepository bookRepository, ShopRepository shopRepository) {
        this.bookRepository = bookRepository;
        this.shopRepository = shopRepository;
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
        mav.addObject("book", new BookDto());
        mav.addObject("allShops", shopRepository.findAll());
        return mav;
    }

    @PostMapping()
    public String saveBook(@ModelAttribute BookDto bookDto) {
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPrice(bookDto.getPrice());
        book.setShops(shopRepository.findAllById(bookDto.getShopIds()));
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/edit")
    public ModelAndView editBookForm(@AuthenticationPrincipal CustomUserDetails user,
                                     @RequestParam Long bookId) {
        ModelAndView mav = new ModelAndView("book-form");
        mav.addObject("user", user);
        bookRepository.findById(bookId).ifPresent(
                book -> mav.addObject("book", BookDto.of(book))
        );
        mav.addObject("allShops", shopRepository.findAll());
        return mav;
    }

    @GetMapping("/delete")
    public String deleteBook(@RequestParam Long bookId) {
        bookRepository.deleteById(bookId);
        return "redirect:/books";
    }

}
