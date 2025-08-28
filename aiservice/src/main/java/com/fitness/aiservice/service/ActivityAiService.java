package com.fitness.aiservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recomendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAiService {

    public final GeminiService geminiService;

    public Recomendation generateRecommendation(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getAnswer(prompt);
        
        return processAiResponse(activity, aiResponse);
    }

    private Recomendation processAiResponse(Activity activity, String aiResponse){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(aiResponse);
            JsonNode analysisNode = root.path("candidates")
                                         .get(0)
                                         .path("content")
                                         .path("parts")
                                         .get(0)
                                         .path("text");
            String jsonContent = analysisNode.asText()
                                            .replaceAll("```json\\n", "")
                                            .replaceAll("\n```", "")
                                            .trim();
            // log.info("processed AI Response: {}", jsonContent);

            JsonNode analysisJson = objectMapper.readTree(jsonContent);
            JsonNode analysisNode1 = analysisJson.path("analysis");
            StringBuilder fullAnalysis  = new StringBuilder();
            addAnalysisSection(fullAnalysis, analysisNode1, "overall","Overall");
            addAnalysisSection(fullAnalysis, analysisNode1, "pace","Pace");
            addAnalysisSection(fullAnalysis, analysisNode1, "heartRate","Heart Rate");
            addAnalysisSection(fullAnalysis, analysisNode1, "caloriesBurned","Calories Burned");
            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));
             return Recomendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserid())
                .activityType(activity.getType())
                .recomendation(fullAnalysis.toString())
                .improvements(improvements)
                .suggestions(suggestions)
                .safety(safety)
                .createdAt(LocalDateTime.now())
                .build();
        } catch (Exception e) {
            e.printStackTrace();
            return createDefaultRecommendation(activity);
        }
    }

    private Recomendation createDefaultRecommendation(Activity activity) {
        return Recomendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserid())
                .activityType(activity.getType())
                .recomendation("No detailed analysis available")
                .improvements(Collections.singletonList("No improvements found"))
                .suggestions(Collections.singletonList("No suggestions found"))
                .safety(Collections.singletonList("No safety guidelines found"))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
        List<String> safetyGuidelines = new ArrayList<>();
        if (safetyNode.isArray()){
            safetyNode.forEach(item ->{
                safetyGuidelines.add(item.asText());
            });
        }
        return safetyGuidelines.isEmpty() ?
                Collections.singletonList("No safety guidelines found") : safetyGuidelines;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestions  = new ArrayList<>();
        if (suggestionsNode.isArray()){
            suggestionsNode.forEach(item ->{
                String workout = item.path("workout").asText();
                String description = item.path("description").asText();
                suggestions.add(workout + ": " + description);

            });
        }
        return suggestions.isEmpty() ?
                Collections.singletonList("No suggestions found") : suggestions;
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements = new ArrayList<>();
        if (improvementsNode.isArray()) {
            improvementsNode.forEach(item ->{
                String area = item.path("area").asText();
                String detail = item.path("recommendation").asText();
                improvements.add(area + ": " + detail);
            });
        }
        return improvements.isEmpty() ?
                Collections.singletonList("No improvements found") : improvements;
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode1, String key, String prefix) {
        if (!analysisNode1.path(key).isMissingNode()) {
            fullAnalysis.append(prefix).append(analysisNode1.path(key).asText()).append("\n\n");
        }
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
            
                Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
        {
          "analysis": {
            "overall": "Overall analysis here",
            "pace": "Pace analysis here",
            "heartRate": "Heart rate analysis here",
            "caloriesBurned": "Calories analysis here"
          },
          "improvements": [
            {
              "area": "Area name",
              "recommendation": "Detailed recommendation"
            }
          ],
          "suggestions": [
            {
              "workout": "Workout name",
              "description": "Detailed workout description"
            }
          ],
          "safety": [
            "Safety point 1",
            "Safety point 2"
          ]
        }

        Analyze this activity:
        Activity Type: %s
        Duration: %d minutes
        Calories Burned: %d
        Additional Metrics: %s
        
        Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
        Ensure the response follows the EXACT JSON format shown above.
        """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics());

    }

}


