package project.votebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import project.votebackend.type.Gender;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UserSignupDto {
    private String username;
    private String password;
    private String name;
    private Gender gender;
    private String phone;
    private LocalDate birthdate;
    private String address;
    private String introduction;
    private String profileImage;
    private List<Long> interestCategory;
}
