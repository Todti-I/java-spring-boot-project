package ru.vedeshkin.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.vedeshkin.project.entity.Shop;
import ru.vedeshkin.project.model.CustomUserDetails;
import ru.vedeshkin.project.repository.ShopRepository;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/shops")
public class ShopController {

    private final ShopRepository shopRepository;

    @Autowired
    public ShopController(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @GetMapping()
    public ModelAndView getAllShops(@AuthenticationPrincipal CustomUserDetails user) {
        ModelAndView mav = new ModelAndView("list-shops");
        mav.addObject("user", user);
        mav.addObject("shops", shopRepository.findAll());
        return mav;
    }

    @GetMapping("/add")
    public ModelAndView addShopForm(@AuthenticationPrincipal CustomUserDetails user) {
        ModelAndView mav = new ModelAndView("shop-form");
        mav.addObject("user", user);
        mav.addObject("shop", new Shop());
        return mav;
    }

    @PostMapping()
    public String saveShop(@ModelAttribute Shop shop) {
        shopRepository.save(shop);
        return "redirect:/shops";
    }

    @GetMapping("/edit")
    public ModelAndView editShopForm(@AuthenticationPrincipal CustomUserDetails user,
                                     @RequestParam Long shopId) {
        ModelAndView mav = new ModelAndView("shop-form");
        Optional<Shop> optionalShop = shopRepository.findById(shopId);
        Shop shop = new Shop();
        if (optionalShop.isPresent()) {
            shop = optionalShop.get();
        }
        mav.addObject("user", user);
        mav.addObject("shop", shop);
        return mav;
    }

    @GetMapping("/delete")
    public String deleteShop(@RequestParam Long shopId) {
        shopRepository.deleteById(shopId);
        return "redirect:/shops";
    }

}
