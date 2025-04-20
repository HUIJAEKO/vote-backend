package project.votebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class VoteResultStatisticsDto {
    private Map<String, Long> stat;
}
