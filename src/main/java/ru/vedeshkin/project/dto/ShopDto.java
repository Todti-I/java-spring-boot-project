package ru.vedeshkin.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vedeshkin.project.entity.Shop;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopDto {

    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String address;

    public static ShopDto of(Shop shop) {
        ShopDto dto = new ShopDto();
        dto.setId(shop.getId());
        dto.setName(shop.getName());
        dto.setAddress(shop.getAddress());

        return dto;
    }

}
