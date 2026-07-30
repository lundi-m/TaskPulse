package com.lundi_m.taskpulse.integration;

import com.lundi_m.taskpulse.model.entity.MoodEntry;
import com.lundi_m.taskpulse.model.entity.Task;
import com.lundi_m.taskpulse.model.entity.TaskPulseUser;
import com.lundi_m.taskpulse.model.enums.DifficultyLevel;
import com.lundi_m.taskpulse.model.enums.EnergyLevel;
import com.lundi_m.taskpulse.model.enums.MoodType;
import com.lundi_m.taskpulse.model.enums.Priority;
import com.lundi_m.taskpulse.repository.MoodEntryRepository;
import com.lundi_m.taskpulse.repository.TaskRepository;
import com.lundi_m.taskpulse.repository.UserRepository;
import com.lundi_m.taskpulse.testUtil.TaskData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.lundi_m.taskpulse.testUtil.MoodData.createMood;
import static com.lundi_m.taskpulse.testUtil.UserData.createUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RecommendationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MoodEntryRepository moodRepository;

    @Test
    @WithMockUser(username = "johndoe@gmail.com")
    void shouldRecommendBestTask() throws Exception{

        TaskPulseUser user = userRepository.save(createUser());

        Task easyTask = TaskData.createTask(user,
                "easy",
                DifficultyLevel.EASY,
                Priority.LOW,
                30,
                null);

        Task veryEasyTask = TaskData.createTask(user,
                "very easy",
                DifficultyLevel.VERY_EASY,
                Priority.LOW,
                50,
                null);

        Task hardTask = TaskData.createTask(user,
                "hard",
                DifficultyLevel.HARD,
                Priority.HIGH,
                40,
                null);

        taskRepository.saveAll(List.of(easyTask, veryEasyTask, hardTask));

        MoodEntry mood = createMood(user,
                MoodType.LOW,
                EnergyLevel.LOW,
                30);

        moodRepository.save(mood);

        mockMvc.perform(get("/recommendations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(easyTask.getTitle()))
                .andExpect(jsonPath("$.score").isNumber())
                .andExpect(jsonPath("$.reasoning").isString());
    }

    @Test
    @WithMockUser(username = "johndoe@gmail.com")
    void shouldReturnNotFoundWhenNoMoodExists()  throws Exception{

        TaskPulseUser user = userRepository.save(createUser());

        Task task = TaskData.createTask(user,
                "Read emails",
                DifficultyLevel.EASY,
                Priority.LOW,
                15,
                LocalDate.now().plusDays(3));

        taskRepository.save(task);

        mockMvc.perform(get("/recommendations"))
                .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser(username = "johndoe@gmail.com")
    void shouldReturnNotFoundWhenNoIncompleteTasksExist() throws Exception{

        TaskPulseUser user = userRepository.save(createUser());

        MoodEntry mood = createMood(user,
                MoodType.ENERGIZED,
                EnergyLevel.PEAK,
                120);

        moodRepository.save(mood);

        mockMvc.perform(get("/recommendations"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "johndoe@gmail.com")
    void shouldReturnRankedTasks() throws Exception{

        TaskPulseUser user = userRepository.save(createUser());

        Task task = TaskData.createTask(user,
                "Assignment",
                "Write and submit the assignment before the due date",
                DifficultyLevel.HARD,
                Priority.HIGH,
                60,
                LocalDate.now().plusDays(3));

        Task task2 = TaskData.createTask(user,
                "Take out the trash bag",
                DifficultyLevel.VERY_EASY,
                Priority.MEDIUM,
                5,
                LocalDate.now().plusDays(2));

        Task task3 = TaskData.createTask(user,
                "Gym",
                DifficultyLevel.HARD,
                Priority.LOW,
                45,
                null);

        taskRepository.saveAll(List.of(task, task2, task3));

        MoodEntry mood = createMood(user,
                MoodType.LOW,
                EnergyLevel.LOW,
                25);

        moodRepository.save(mood);

        mockMvc.perform(get("/recommendations/ranked"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].title").value(task2.getTitle()))
                .andExpect(jsonPath("$[2].reasoning").exists());

    }

    @Test
    @WithMockUser(username = "johndoe@gmail.com")
    void shouldIgnoreCompletedTasksWhenRecommending() throws Exception{

        TaskPulseUser user = userRepository.save(createUser());

        Task completed = TaskData.createTask(user,
                "Completed",
                DifficultyLevel.EASY,
                Priority.LOW,
                30,
                null);

        completed.setCompleted("Completed");

        Task notCompleted = TaskData.createTask(user,
                "Not completed",
                DifficultyLevel.VERY_EASY,
                Priority.LOW,
                50,
                null);

        taskRepository.saveAll(List.of(notCompleted, completed));

        MoodEntry mood = createMood(user,
                MoodType.ENERGIZED,
                EnergyLevel.PEAK,
                180);

        moodRepository.save(mood);

        mockMvc.perform(get("/recommendations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Not completed"));
    }
}
