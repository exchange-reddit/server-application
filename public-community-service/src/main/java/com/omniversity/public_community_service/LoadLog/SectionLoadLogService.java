package com.omniversity.public_community_service.LoadLog;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class SectionLoadLogService {
    // Using a thread safe data type to store the access counts of a section
    private final ConcurrentHashMap<Long, AtomicInteger> sectionHits = new ConcurrentHashMap<>();

    public void recordAccess(Long sectionId) {
        sectionHits.computeIfAbsent(sectionId, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public int getViewCount(Long sectionId) {
        return sectionHits.getOrDefault(sectionId, new AtomicInteger(0)).get();
    }

    public Map<Long, Integer> getAllViewCount() {
        return sectionHits.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    }
}
