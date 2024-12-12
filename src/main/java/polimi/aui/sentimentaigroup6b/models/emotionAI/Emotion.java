package polimi.aui.sentimentaigroup6b.models.emotionAI;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Emotion {

    ANGER("Anger"),
    DISGUST("Disgust"),
    FEAR("Fear"),
    JOY("Joy"),
    SADNESS("Sadness"),
    SURPRISE("Surprise"),
    NEUTRALITY("Neutrality");

    private final String emotion;
}
