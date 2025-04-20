package project.votebackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.votebackend.dto.VoteResultStatisticsDto;
import project.votebackend.repository.VoteSelectRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VoteResultService {

    private final VoteSelectRepository voteSelectionsRepository;

    // 주요 행정 구역 목록
    private final List<String> koreanRegions = List.of(
            "서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시",
            "대전광역시", "울산광역시", "세종특별자치시", "경기도", "강원도",
            "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주특별자치도"
    );

    /**
     * 성별 기준 통계 조회
     *
     * @param voteId 투표 ID
     * @return Map<성별, 옵션별 통계>
     */
    public Map<String, VoteResultStatisticsDto> getGenderStats(Long voteId) {
        List<Object[]> data = voteSelectionsRepository.findGenderStatistics(voteId);
        return groupByCategory(data); // 성별은 그대로 사용 가능
    }

    /**
     * 연령대 기준 통계 조회
     *
     * @param voteId 투표 ID
     * @return Map<연령대, 옵션별 통계>
     */
    public Map<String, VoteResultStatisticsDto> getAgeStats(Long voteId) {
        List<Object[]> data = voteSelectionsRepository.findAgeStatistics(voteId);
        Map<String, VoteResultStatisticsDto> result = new HashMap<>();

        for (Object[] row : data) {
            LocalDate birth = (LocalDate) row[0];
            String option = (String) row[1];
            String ageGroup = getAgeGroup(birth);

            result.computeIfAbsent(ageGroup, k -> new VoteResultStatisticsDto(new HashMap<>()))
                    .getStat()
                    .merge(option, 1L, Long::sum);
        }
        return result;
    }

    /**
     * 지역 기준 통계 조회
     *
     * @param voteId 투표 ID
     * @return Map<지역명, 옵션별 통계>
     */
    public Map<String, VoteResultStatisticsDto> getRegionStats(Long voteId) {
        List<Object[]> data = voteSelectionsRepository.findRegionStatistics(voteId);
        Map<String, VoteResultStatisticsDto> result = new HashMap<>();

        for (Object[] row : data) {
            String address = (String) row[0];
            String option = (String) row[1];
            String region = extractRegion(address);

            result.computeIfAbsent(region, k -> new VoteResultStatisticsDto(new HashMap<>()))
                    .getStat()
                    .merge(option, 1L, Long::sum);
        }
        return result;
    }

    /**
     * 생년월일 → 연령대 문자열로 변환
     *
     * @param birthDate LocalDate
     * @return 연령대 문자열
     */
    private String getAgeGroup(LocalDate birthDate) {
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < 10) return "10대 미만";
        if (age < 20) return "10대";
        if (age < 30) return "20대";
        if (age < 40) return "30대";
        if (age < 50) return "40대";
        return "50대 이상";
    }

    /**
     * 주소에서 시/도명 추출
     *
     * @param address 유저 주소
     * @return 시/도 또는 "기타"
     */
    private String extractRegion(String address) {
        return koreanRegions.stream()
                .filter(address::startsWith)
                .findFirst()
                .orElse("기타");
    }

    /**
     * 공통 분류 메서드: 성별/기타 항목을 기준으로 옵션별 카운트 누적
     *
     * @param data Object[] = [기준값, 옵션명]
     * @return Map<기준값, 옵션별 통계>
     */
    private Map<String, VoteResultStatisticsDto> groupByCategory(List<Object[]> data) {
        Map<String, VoteResultStatisticsDto> result = new HashMap<>();
        for (Object[] row : data) {
            String group = row[0].toString(); // ex: MALE, FEMALE
            String option = (String) row[1];

            result.computeIfAbsent(group, k -> new VoteResultStatisticsDto(new HashMap<>()))
                    .getStat()
                    .merge(option, 1L, Long::sum);
        }
        return result;
    }
}
