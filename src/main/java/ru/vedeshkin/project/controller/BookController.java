package ru.vedeshkin.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import ru.vedeshkin.project.dto.BookDto;
import ru.vedeshkin.project.entity.Book;
import ru.vedeshkin.project.entity.Shop;
import ru.vedeshkin.project.entity.User;
import ru.vedeshkin.project.model.CustomUserDetails;
import ru.vedeshkin.project.service.BookService;
import ru.vedeshkin.project.service.ShopService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final ShopService shopService;

    @Autowired
    public BookController(BookService bookService, ShopService shopService) {
        this.bookService = bookService;
        this.shopService = shopService;
    }

    @GetMapping()
    public ModelAndView getAllBooks(@AuthenticationPrincipal CustomUserDetails user) {
        List<Book> books = bookService.findAll();

        if (!user.canRead()) {
            books = books.stream()
                    .filter(book -> {
                        User ownerUser = book.getOwnerUser();
                        if (ownerUser == null) {
                            return false;
                        }
                        return ownerUser.getId().equals(user.getId());
                    })
                    .toList();
        }

        ModelAndView mav = new ModelAndView("list-books");
        mav.addObject("user", user);
        mav.addObject("books", books);

        return mav;
    }

    @GetMapping("/add")
    public ModelAndView addBookForm(@AuthenticationPrincipal CustomUserDetails user) {
        List<Shop> shops = shopService.findAll();

        if (!user.canRead()) {
            shops = shops.stream()
                    .filter(shop -> {
                        User ownerUser = shop.getOwnerUser();
                        if (ownerUser == null) {
                            return false;
                        }
                        return ownerUser.getId().equals(user.getId());
                    })
                    .toList();
        }

        ModelAndView mav = new ModelAndView("book-form");
        mav.addObject("user", user);
        mav.addObject("book", new BookDto());
        mav.addObject("allShops", shops);

        return mav;
    }

    @PostMapping()
    public String saveBook(@AuthenticationPrincipal CustomUserDetails user,
                           @ModelAttribute BookDto bookDto) {
        if (!user.canEdit()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (bookDto.getId() != null && !user.isAdmin()) {
            Optional<Book> optionalBook = bookService.findById(bookDto.getId());

            if (optionalBook.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            User ownerUser = optionalBook.get().getOwnerUser();
            if (ownerUser == null || !ownerUser.getId().equals(user.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }

        bookService.save(bookDto, user.getUserEntity());

        return "redirect:/books";
    }

    @GetMapping("/edit")
    public ModelAndView editBookForm(@AuthenticationPrincipal CustomUserDetails user,
                                     @RequestParam Long bookId) {
        List<Shop> shops = shopService.findAll();

        if (!user.canRead()) {
            shops = shops.stream()
                    .filter(shop -> {
                        User ownerUser = shop.getOwnerUser();
                        if (ownerUser == null) {
                            return false;
                        }
                        return ownerUser.getId().equals(user.getId());
                    })
                    .toList();
        }

        ModelAndView mav = new ModelAndView("book-form");
        mav.addObject("user", user);
        bookService.findById(bookId).ifPresent(
                book -> mav.addObject("book", BookDto.of(book))
        );
        mav.addObject("allShops", shops);

        return mav;
    }

    @GetMapping("/delete")
    public String deleteBook(@AuthenticationPrincipal CustomUserDetails user,
                             @RequestParam Long bookId) {
        if (!user.canEdit()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (!user.isAdmin()) {
            Optional<Book> optionalBook = bookService.findById(bookId);

            if (optionalBook.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            User ownerUser = optionalBook.get().getOwnerUser();
            if (ownerUser == null || !ownerUser.getId().equals(user.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }

        bookService.deleteById(bookId);

        return "redirect:/books";
    }

}
