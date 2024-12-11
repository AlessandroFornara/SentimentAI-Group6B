package polimi.aui.sentimentaigroup6b.models;

public enum Activity {
    PERSONALIZED_QUOTE("Personalized Inspirational Quote"),
    SELF_CARE_SUGGESTION("Self-Care Suggestion"),
    CREATIVE_MINI_ACTIVITY("Creative Mini Activity"),
    SHORT_GUIDED_MEDITATION("Short Guided Meditation"),
    CHALLENGE("Challenge"),
    RECOMMENDED_MUSIC("Recommended Music"),;

    private final String description;

    Activity(String description) {
        this.description = description;
    }

    public String getDescription(Emotion detectedEmotion) {
        return switch (detectedEmotion) {
            case JOY -> switch (this) {
                case PERSONALIZED_QUOTE ->
                        "Happiness is not a destination, but a journey. Enjoy every step!";
                case SELF_CARE_SUGGESTION ->
                        "You are feeling joyful! Here is a self-care suggestion to keep you feeling good";
                case CREATIVE_MINI_ACTIVITY ->
                        "Take a photo of something that represents your happiness and save it as a memory";
                case SHORT_GUIDED_MEDITATION ->
                        "You are feeling joyful! Here is a short guided meditation to keep you relaxed";
                case CHALLENGE ->
                        "Today, give someone a sincere compliment or thank a person who made your day better.";
                case RECOMMENDED_MUSIC -> "A playlist with cheerful songs to extend your good mood.";
            };
            case SADNESS -> switch (this) {
                case PERSONALIZED_QUOTE ->
                        "Sadness is a part of life, but also an opportunity to rediscover what truly matters";
                case SELF_CARE_SUGGESTION ->
                        "You are feeling sad! Here is a self-care suggestion to keep you feeling good";
                case CREATIVE_MINI_ACTIVITY ->
                        "Write a paragraph about a happy moment that makes you feel grateful, even when you’re feeling sad";
                case SHORT_GUIDED_MEDITATION ->
                        "A short 3–5 minute meditation to ease sadness and regain some peace of mind.";
                case CHALLENGE ->
                        "Today, write down three things you’re grateful for. Focusing on the good in your life can help improve your mood.";
                case RECOMMENDED_MUSIC -> "A playlist with calming tracks to encourage relaxation.";
            };
            case ANGER -> switch (this) {
                case PERSONALIZED_QUOTE ->
                        "You are feeling angry! Here is an inspirational quote to keep you motivated";
                case SELF_CARE_SUGGESTION ->
                        "A 1–2 minute guided breathing exercise to reduce tension.";
                case CREATIVE_MINI_ACTIVITY ->
                        "Take 5 minutes to step away from what made you angry and focus on an activity that relaxes you.";
                case SHORT_GUIDED_MEDITATION ->
                        "A targeted meditation to calm anger and refocus the mind";
                case CHALLENGE ->
                        "You are feeling angry! Here is a gratitude challenge to keep you feeling positive";
                case RECOMMENDED_MUSIC ->
                        "You are feeling angry! Here is some recommended music to keep you entertained";
            };
            case FEAR -> switch (this) {
                case PERSONALIZED_QUOTE ->
                        "You are feeling fearful! Here is an inspirational quote to keep you motivated";
                case SELF_CARE_SUGGESTION ->
                        "You are feeling fearful! Here is a self-care suggestion to keep you feeling good";
                case CREATIVE_MINI_ACTIVITY ->
                        "You are feeling fearful! Here is a creative mini activity to keep you entertained";
                case SHORT_GUIDED_MEDITATION ->
                        "You are feeling fearful! Here is a short guided meditation to keep you relaxed";
                case CHALLENGE ->
                        "You are feeling fearful! Here is a motivational challenge to keep you feeling motivated";
                case RECOMMENDED_MUSIC ->
                        "You are feeling fearful! Here is some recommended music to keep you entertained";
            };
            case DISGUST -> switch (this) {
                case PERSONALIZED_QUOTE ->
                        "You are feeling disgusted! Here is an inspirational quote to keep you motivated";
                case SELF_CARE_SUGGESTION ->
                        "You are feeling disgusted! Here is a self-care suggestion to keep you feeling good";
                case CREATIVE_MINI_ACTIVITY ->
                        "You are feeling disgusted! Here is a creative mini activity to keep you entertained";
                case SHORT_GUIDED_MEDITATION ->
                        "You are feeling disgusted! Here is a short guided meditation to keep you relaxed";
                case CHALLENGE ->
                        "You are feeling disgusted! Here is a gratitude challenge to keep you feeling positive";
                case RECOMMENDED_MUSIC ->
                        "You are feeling disgusted! Here is some recommended music to keep you entertained";
            };
            case SURPRISE -> switch (this) {
                case PERSONALIZED_QUOTE ->
                        "You are feeling surprised! Here is an inspirational quote to keep you motivated";
                case SELF_CARE_SUGGESTION ->
                        "You are feeling surprised! Here is a self-care suggestion to keep you feeling good";
                case CREATIVE_MINI_ACTIVITY ->
                        "You are feeling surprised! Here is a creative mini activity to keep you entertained";
                case SHORT_GUIDED_MEDITATION ->
                        "You are feeling surprised! Here is a short guided meditation to keep you relaxed";
                case CHALLENGE ->
                        "You are feeling surprised! Here is a motivational challenge to keep you feeling motivated";
                case RECOMMENDED_MUSIC ->
                        "You are feeling surprised! Here is some recommended music to keep you entertained";
            };
            case NEUTRALITY -> switch (this) {
                case PERSONALIZED_QUOTE ->
                        "You are feeling neutral! Here is an inspirational quote to keep you motivated";
                case SELF_CARE_SUGGESTION ->
                        "You are feeling neutral! Here is a self-care suggestion to keep you feeling good";
                case CREATIVE_MINI_ACTIVITY ->
                        "You are feeling neutral! Here is a creative mini activity to keep you entertained";
                case SHORT_GUIDED_MEDITATION ->
                        "You are feeling neutral! Here is a short guided meditation to keep you relaxed";
                case CHALLENGE ->
                        "You are feeling neutral! Here is a motivational challenge to keep you feeling motivated";
                case RECOMMENDED_MUSIC ->
                        "You are feeling neutral! Here is some recommended music to keep you entertained";
            };
        };
    }

}
