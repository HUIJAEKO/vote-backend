package project.votebackend.dto;

import lombok.Data;
import project.votebackend.type.Gender;

import java.time.LocalDate;

@Data
public class UserSignupDto {
    private String username;
    private String password;
    private String name;
    private Gender gender;
    private String phone;
    private LocalDate birthDate;
    private String address;
    private String introduction;
    private String profileImage;
}
