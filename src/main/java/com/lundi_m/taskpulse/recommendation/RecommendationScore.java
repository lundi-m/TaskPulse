package com.lundi_m.taskpulse.recommendation;

import com.lundi_m.taskpulse.model.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendationScore {
    private final Task task;
    private final double score;
}
