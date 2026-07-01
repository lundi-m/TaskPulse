package com.lundi_m.taskpulse.service;

import com.lundi_m.taskpulse.dto.MoodRequest;
import com.lundi_m.taskpulse.dto.MoodResponse;
import com.lundi_m.taskpulse.exception.MoodNotFoundException;
import com.lundi_m.taskpulse.model.entity.MoodEntry;
import com.lundi_m.taskpulse.model.entity.TaskPulseUser;
import com.lundi_m.taskpulse.repository.MoodEntryRepository;
import com.lundi_m.taskpulse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoodService {

    private static final int MAX_MOOD_HISTORY = 10;

    private final MoodEntryRepository moodEntryRepository;
    private final UserRepository userRepository;

    @Transactional
    public MoodResponse logMood(String email, MoodRequest request){

        TaskPulseUser user = getUser(email);

        //Auto_cleanup - delete oldest if at limit
        if (moodEntryRepository.countByUserId(user.getId()) >= MAX_MOOD_HISTORY){
            moodEntryRepository
                    .findTopByUserIdOrderByRecordedAtAsc(user.getId())
                    .ifPresent(moodEntryRepository::delete);
        }

        MoodEntry entry = MoodEntry.builder()
                .user(user)
                .moodType(request.getMoodType())
                .energyLevel(request.getEnergyLevel())
                .availableTime(request.getAvailableTime())
                .recordedAt(Instant.now())
                .build();

        MoodEntry saved = moodEntryRepository.save(entry);

        return mapToDTO(saved);
    }

    public MoodResponse getCurrentMood(String email){
        TaskPulseUser user = getUser(email);

        MoodEntry entry = moodEntryRepository.findTopByUserIdOrderByRecordedAtDesc(user.getId())
                .orElseThrow(MoodNotFoundException::new);

        return mapToDTO(entry);
    }

    public List<MoodResponse> getHistory(String email){

        TaskPulseUser user = getUser(email);

        List<MoodEntry> entries = moodEntryRepository.findTop10ByUserIdOrderByRecordedAtDesc(user.getId());

        return entries.stream()
                .map(this::mapToDTO)
                .toList();
    }

    private TaskPulseUser getUser(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Mapper
    private MoodResponse mapToDTO(MoodEntry moodEntry){
        return MoodResponse.builder()
                .moodType(moodEntry.getMoodType())
                .energyLevel(moodEntry.getEnergyLevel())
                .availableTime(moodEntry.getAvailableTime())
                .recordedAt(moodEntry.getRecordedAt())
                .build();
    }
}
