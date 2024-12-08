package polimi.aui.sentimentaigroup6b.models;

import polimi.aui.sentimentaigroup6b.entities.Badge;

import java.util.List;


public class FinalResponse {

    private String dominantEmotion;

    private String activityCategory;
    private String activityText;

    private List<Badge> badges;

    public FinalResponse(String dominantEmotion, String activityCategory, String activityText, List<Badge> badges) {
        this.dominantEmotion = dominantEmotion;
        this.activityCategory = activityCategory;
        this.activityText = activityText;
        this.badges = badges;
    }

}
