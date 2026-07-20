package com.lundi_m.taskpulse.service;

import com.lundi_m.taskpulse.dto.recommendation.RecommendationResponse;
import com.lundi_m.taskpulse.exception.MoodNotFoundException;
import com.lundi_m.taskpulse.exception.NoIncompleteTasksFoundException;
import com.lundi_m.taskpulse.exception.RecommendationGenerationException;
import com.lundi_m.taskpulse.model.entity.MoodEntry;
import com.lundi_m.taskpulse.model.entity.Task;
import com.lundi_m.taskpulse.model.entity.TaskPulseUser;
import com.lundi_m.taskpulse.recommendation.ReasoningEngine;
import com.lundi_m.taskpulse.recommendation.RecommendationScore;
import com.lundi_m.taskpulse.recommendation.ScoringEngine;
import com.lundi_m.taskpulse.repository.MoodEntryRepository;
import com.lundi_m.taskpulse.repository.TaskRepository;
import com.lundi_m.taskpulse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final ScoringEngine scoringEngine;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final MoodEntryRepository moodEntryRepository;
    private final ReasoningEngine reasoningEngine;

    public RecommendationResponse recommend(String email){
        TaskPulseUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        MoodEntry currentMood = moodEntryRepository.findTopByUserIdOrderByRecordedAtDesc(user.getId())
                .orElseThrow(MoodNotFoundException::new);

        List<Task> tasks = taskRepository
                .findFiltered(user.getId(), "Not completed", null, Pageable.unpaged())
                .getContent();

        if (tasks.isEmpty()){
            throw new NoIncompleteTasksFoundException();
        }

        RecommendationScore result = scoringEngine.recommend(tasks, currentMood)
                .orElseThrow(RecommendationGenerationException::new);

        return mapToDTO(result, currentMood);
    }

    public List<RecommendationResponse> rankALl(String email){
        TaskPulseUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        MoodEntry currentMood = moodEntryRepository.findTopByUserIdOrderByRecordedAtDesc(user.getId())
                .orElseThrow(MoodNotFoundException::new);

        List<Task> tasks = taskRepository
                .findFiltered(user.getId(), "Not completed", null, Pageable.unpaged())
                .getContent();

        return scoringEngine.rankAll(tasks, currentMood)
                .stream()
                .map(results -> this.mapToDTO(results, currentMood))
                .toList();
    }

    private RecommendationResponse mapToDTO(RecommendationScore recommendationScore, MoodEntry currentMood) {

        return RecommendationResponse.builder()
                .taskId(recommendationScore.getTask().getId())
                .title(recommendationScore.getTask().getTitle())
                .description(recommendationScore.getTask().getDescription())
                .priority(recommendationScore.getTask().getPriority())
                .difficultyLevel(recommendationScore.getTask().getDifficultyLevel())
                .estimatedDuration(recommendationScore.getTask().getEstimatedDuration())
                .deadline(recommendationScore.getTask().getDeadline())
                .score(recommendationScore.getScore())
                .reasoning(reasoningEngine.buildRecommendationReasoning(recommendationScore, currentMood))
                .build();
    }
}
