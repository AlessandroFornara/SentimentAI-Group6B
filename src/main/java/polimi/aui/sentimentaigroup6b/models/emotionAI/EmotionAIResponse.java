package polimi.aui.sentimentaigroup6b.models.emotionAI;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@ToString
public class EmotionAIResponse {

    private Map<Emotion, Double> emotions;

    public EmotionAIResponse(double anger, double disgust, double fear, double joy, double sadness, double surprise, double neutrality) {
        this.emotions = new LinkedHashMap<>();
        this.emotions.put(Emotion.ANGER, anger);
        this.emotions.put(Emotion.DISGUST, disgust);
        this.emotions.put(Emotion.FEAR, fear);
        this.emotions.put(Emotion.JOY, joy);
        this.emotions.put(Emotion.SADNESS, sadness);
        this.emotions.put(Emotion.SURPRISE, surprise);
        this.emotions.put(Emotion.NEUTRALITY, neutrality);
    }

    public List<Double> getEmotionsAsList(){
        return new ArrayList<>(emotions.values());
    }
}
