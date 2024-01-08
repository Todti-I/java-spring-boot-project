package ru.vedeshkin.project.service;

import org.springframework.stereotype.Service;
import ru.vedeshkin.project.dto.ShopDto;
import ru.vedeshkin.project.entity.Book;
import ru.vedeshkin.project.entity.Shop;
import ru.vedeshkin.project.entity.User;
import ru.vedeshkin.project.repository.ShopRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;

    public ShopServiceImpl(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public Optional<Shop> findById(Long id) {
        return shopRepository.findById(id);
    }

    @Override
    public List<Shop> findAll() {
        return shopRepository.findAll();
    }

    @Override
    public void save(ShopDto shopDto, User currentUser) {
        Shop shop = new Shop();
        if (shopDto.getId() != null) {
            Optional<Shop> optionalShop = shopRepository.findById(shopDto.getId());
            if (optionalShop.isPresent()) {
                shop = optionalShop.get();
            }
        } else {
            shop.setOwnerUser(currentUser);
        }
        shop.setName(shopDto.getName());
        shop.setAddress(shopDto.getAddress());
        shopRepository.save(shop);
    }

    @Override
    public void deleteById(Long id) {
        shopRepository.deleteById(id);
    }

}
