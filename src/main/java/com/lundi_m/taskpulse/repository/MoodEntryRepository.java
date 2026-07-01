package com.lundi_m.taskpulse.repository;

import com.lundi_m.taskpulse.model.entity.MoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoodEntryRepository extends JpaRepository<MoodEntry, Long> {

    // Latest entry - used by the recommendation engine
    Optional<MoodEntry> findTopByUserIdOrderByRecordedAtDesc(Long userId);

    // Last 10 entries - used for history display
    List<MoodEntry> findTop10ByUserIdOrderByRecordedAtDesc(Long userId);

    // Count entries - used for auto-cleanup check
    long countByUserId(Long userId);

    // Oldest entry - deleted when limit is exceeded
    Optional<MoodEntry> findTopByUserIdOrderByRecordedAtAsc(Long userId);
}
