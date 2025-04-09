package project.votebackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.votebackend.service.MigrationService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/migrate")
public class MigrationController {
    private final MigrationService migrationService;

    @PostMapping("/votes")
    public String migrateVotes() {
        try {
            migrationService.migrateVotesToElasticsearch();
            return "Migration success";
        } catch (IOException e) {
            return "Migration failed";
        }
    }
}
