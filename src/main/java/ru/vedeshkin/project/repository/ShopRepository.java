package ru.vedeshkin.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vedeshkin.project.entity.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

}
