package project.votebackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.votebackend.dto.MyPageDto;
import project.votebackend.security.CustumUserDetails;
import project.votebackend.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    //마이페이지 조회
    @GetMapping("/mypage")
    public ResponseEntity<MyPageDto> getMyPage(
            @AuthenticationPrincipal CustumUserDetails userDetails,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        MyPageDto dto = userService.getMyPage(userDetails.getId(), pageable);
        return ResponseEntity.ok(dto);
    }
}
