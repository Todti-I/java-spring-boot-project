package ru.vedeshkin.project.service;

import ru.vedeshkin.project.dto.ShopDto;
import ru.vedeshkin.project.entity.Shop;
import ru.vedeshkin.project.entity.User;

import java.util.List;
import java.util.Optional;

public interface ShopService {

    Optional<Shop> findById(Long id);

    List<Shop> findAll();

    Shop save(ShopDto shopDto, User currentUser);

    void deleteById(Long id);

}
