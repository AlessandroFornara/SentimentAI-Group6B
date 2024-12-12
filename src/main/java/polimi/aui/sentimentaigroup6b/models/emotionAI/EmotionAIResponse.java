package polimi.aui.sentimentaigroup6b.models.emotionAI;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@ToString
public class EmotionAIResponse {

    private Map<Emotion, Double> emotions;

    public EmotionAIResponse(double anger, double disgust, double fear, double joy, double sadness, double surprise, double neutrality) {
        this.emotions = new HashMap<>();
        this.emotions.put(Emotion.ANGER, anger);
        this.emotions.put(Emotion.DISGUST, disgust);
        this.emotions.put(Emotion.FEAR, fear);
        this.emotions.put(Emotion.JOY, joy);
        this.emotions.put(Emotion.SADNESS, sadness);
        this.emotions.put(Emotion.SURPRISE, surprise);
        this.emotions.put(Emotion.NEUTRALITY, neutrality);
    }

    public Emotion getDominantEmotion() {
        return emotions.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Emotion.NEUTRALITY);
    }
}
