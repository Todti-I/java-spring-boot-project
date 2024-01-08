package ru.vedeshkin.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import ru.vedeshkin.project.dto.ShopDto;
import ru.vedeshkin.project.entity.Shop;
import ru.vedeshkin.project.entity.User;
import ru.vedeshkin.project.model.CustomUserDetails;
import ru.vedeshkin.project.service.ActionService;
import ru.vedeshkin.project.service.ShopService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shops")
public class ShopController {

    private final ActionService actionService;
    private final ShopService shopService;

    @Autowired
    public ShopController(ActionService actionService, ShopService shopService) {
        this.actionService = actionService;
        this.shopService = shopService;
    }

    @GetMapping()
    public ModelAndView getAllShops(@AuthenticationPrincipal CustomUserDetails user) {
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

        ModelAndView mav = new ModelAndView("list-shops");
        mav.addObject("user", user);
        mav.addObject("shops", shops);

        return mav;
    }

    @GetMapping("/add")
    public ModelAndView addShopForm(@AuthenticationPrincipal CustomUserDetails user) {
        ModelAndView mav = new ModelAndView("shop-form");
        mav.addObject("user", user);
        mav.addObject("shop", new ShopDto());

        return mav;
    }

    @PostMapping()
    public String saveShop(@AuthenticationPrincipal CustomUserDetails user,
                           @ModelAttribute ShopDto shopDto) {
        if (!user.canEdit()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (shopDto.getId() != null && !user.isAdmin()) {
            Optional<Shop> optionalShop = shopService.findById(shopDto.getId());

            if (optionalShop.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            User ownerUser = optionalShop.get().getOwnerUser();
            if (ownerUser == null || !ownerUser.getId().equals(user.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }

        Shop savedShop = shopService.save(shopDto, user.getUserEntity());
        actionService.create(String.format("User %s saved shop %s", user.getId(), savedShop.getId()));

        return "redirect:/shops";
    }

    @GetMapping("/edit")
    public ModelAndView editShopForm(@AuthenticationPrincipal CustomUserDetails user,
                                     @RequestParam Long shopId) {
        ModelAndView mav = new ModelAndView("shop-form");
        mav.addObject("user", user);
        shopService.findById(shopId).ifPresent(shop ->
                mav.addObject("shop", ShopDto.of(shop))
        );

        return mav;
    }

    @GetMapping("/delete")
    public String deleteShop(@AuthenticationPrincipal CustomUserDetails user,
                             @RequestParam Long shopId) {
        if (!user.canEdit()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (!user.isAdmin()) {
            Optional<Shop> optionalShop = shopService.findById(shopId);

            if (optionalShop.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            User ownerUser = optionalShop.get().getOwnerUser();
            if (ownerUser == null || !ownerUser.getId().equals(user.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }

        shopService.deleteById(shopId);
        actionService.create(String.format("User %s deleted shop %s", user.getId(), shopId));

        return "redirect:/shops";
    }

}
