package project.votebackend.util;

import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

public class PageResponseUtil {
    public static <T> Map<String, Object> toResponse(Page<T> page) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("page", page.getNumber());
        response.put("size", page.getSize());
        response.put("totalElements", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        response.put("last", page.isLast());
        return response;
    }
}
