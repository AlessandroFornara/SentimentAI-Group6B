package polimi.aui.sentimentaigroup6b.models;

public enum Emotion {

    ANGER("Anger"),
    DISGUST("Disgust"),
    FEAR("Fear"),
    JOY("Joy"),
    SADNESS("Sadness"),
    SURPRISE("Surprise"),
    NEUTRALITY("Neutrality");

    private final String emotion;

    Emotion(String emotion) {
        this.emotion = emotion;
    }

    public String getEmotion() {
        return emotion;
    }

}
