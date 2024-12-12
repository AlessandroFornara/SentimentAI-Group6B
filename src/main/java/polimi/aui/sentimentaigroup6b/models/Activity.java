package polimi.aui.sentimentaigroup6b.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import polimi.aui.sentimentaigroup6b.models.emotionAI.Emotion;

@AllArgsConstructor
@Getter
public enum Activity {
    PERSONALIZED_QUOTE("Personalized Inspirational Quote"),
    SELF_CARE_SUGGESTION("Self-Care Suggestion"),
    CREATIVE_MINI_ACTIVITY("Creative Mini Activity"),
    SHORT_GUIDED_MEDITATION("Short Guided Meditation"),
    CHALLENGE("Challenge"),
    RECOMMENDED_MUSIC("Recommended Music"),;

    private final String categoryDescription;

    public String assignActivity(Emotion detectedEmotion) {
        return switch (detectedEmotion) {
            case JOY -> switch (this) {
                case PERSONALIZED_QUOTE -> "Happiness is not a destination, but a journey. Enjoy every step!";
                case SELF_CARE_SUGGESTION -> "You are feeling joyful! Take some time to enjoy your favorite activity today.";
                case CREATIVE_MINI_ACTIVITY -> "Take a photo of something that represents your happiness and save it as a memory.";
                case SHORT_GUIDED_MEDITATION -> "You are feeling joyful! Here is a short guided meditation to keep you relaxed.";
                case CHALLENGE -> "Today, give someone a sincere compliment or thank a person who made your day better.";
                case RECOMMENDED_MUSIC -> "A playlist with cheerful songs to extend your good mood.";
            };
            case SADNESS -> switch (this) {
                case PERSONALIZED_QUOTE -> "Sadness is a part of life, but also an opportunity to rediscover what truly matters.";
                case SELF_CARE_SUGGESTION -> "Today, take 15 minutes to listen to your favorite music or read a chapter of your favorite book.";
                case CREATIVE_MINI_ACTIVITY -> "Write a paragraph about a happy moment that makes you feel grateful, even when you’re feeling sad.";
                case SHORT_GUIDED_MEDITATION -> "A short 3–5 minute meditation to ease sadness and regain some peace of mind.";
                case CHALLENGE -> "Today, write down three things you’re grateful for. Focusing on the good in your life can help improve your mood.";
                case RECOMMENDED_MUSIC -> "A playlist with calming tracks to encourage relaxation.";
            };
            case ANGER -> switch (this) {
                case PERSONALIZED_QUOTE -> "Calmness is the power to stay balanced even when everything around you is in chaos.";
                case SELF_CARE_SUGGESTION -> "Take 5 minutes to step away from what made you angry and focus on an activity that relaxes you.";
                case CREATIVE_MINI_ACTIVITY -> "Write down what made you angry today and try to identify a positive aspect or a lesson to learn.";
                case SHORT_GUIDED_MEDITATION -> "A targeted meditation to calm anger and refocus the mind.";
                case CHALLENGE -> "Find a small positive thing today and focus on it, even if it’s simple.";
                case RECOMMENDED_MUSIC -> "A playlist with calming music to ease tension and help you relax.";
            };
            case DISGUST -> switch (this) {
                case PERSONALIZED_QUOTE -> "Acceptance is the key to inner peace.";
                case SELF_CARE_SUGGESTION -> "Spend a few minutes on a pleasant activity, like listening to your favorite music or reading something interesting.";
                case CREATIVE_MINI_ACTIVITY -> "Visualize a place or situation that brings peace and try to draw or write about it.";
                case SHORT_GUIDED_MEDITATION -> "A meditation to help distance yourself from feelings of disgust or discomfort.";
                case CHALLENGE -> "Find one small positive thing about today and focus on it.";
                case RECOMMENDED_MUSIC -> "A playlist with soothing tracks to help you find balance and relaxation.";
            };
            case FEAR -> switch (this) {
                case PERSONALIZED_QUOTE -> "Courage is not the absence of fear, but the decision to move forward anyway.";
                case SELF_CARE_SUGGESTION -> "Take a moment to list the things that make you feel safe and protected.";
                case CREATIVE_MINI_ACTIVITY -> "Write about a time when you overcame a difficult situation to remind yourself of your strength.";
                case SHORT_GUIDED_MEDITATION -> "A guided meditation to calm the mind and reduce anxiety.";
                case CHALLENGE -> "Set a small, achievable goal today to boost your confidence.";
                case RECOMMENDED_MUSIC -> "A playlist with soothing and calming tracks to ease the mind.";
            };
            case NEUTRALITY -> switch (this) {
                case PERSONALIZED_QUOTE -> "Serenity is an inner achievement. Find beauty in the simplicity of your day.";
                case SELF_CARE_SUGGESTION -> "Spend a few minutes on a small pleasurable activity, like reading a chapter of a book or listening to a podcast.";
                case CREATIVE_MINI_ACTIVITY -> "Think of one thing that makes you happy or satisfied and write it down to remember it.";
                case SHORT_GUIDED_MEDITATION -> "A short meditation to encourage relaxation and mindfulness.";
                case CHALLENGE -> "Write down one small positive thing you noticed today, even if it’s simple.";
                case RECOMMENDED_MUSIC -> "A soft playlist to encourage reflection or relaxation.";
            };
            case SURPRISE -> switch (this) {
                case PERSONALIZED_QUOTE -> "Life is full of surprises. Embrace the unexpected!";
                case SELF_CARE_SUGGESTION -> "Take a moment to enjoy the surprise and reflect on what it means to you.";
                case CREATIVE_MINI_ACTIVITY -> "Capture the surprise in a drawing or a short story.";
                case SHORT_GUIDED_MEDITATION -> "A meditation to help you process and enjoy the surprise.";
                case CHALLENGE -> "Share the surprise with a friend or family member and see their reaction.";
                case RECOMMENDED_MUSIC -> "A playlist with upbeat and surprising tracks to match your mood.";
            };
            default -> throw new IllegalStateException("Unexpected emotion value: " + this);
        };
    }


}
