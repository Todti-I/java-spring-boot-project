package ru.vedeshkin.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vedeshkin.project.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    public UserDto(User user) {
        id = user.getId();
        firstName = user.getName();
    }

    public static UserDto mapTo(User user) {
        UserDto userDto = new UserDto();
        String[] names = user.getName().split(" ");
        userDto.setFirstName(names[0]);
        userDto.setLastName(names[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }

}
