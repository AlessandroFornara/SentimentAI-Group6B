package polimi.aui.sentimentaigroup6b.models;

public class ActivityResponse {

    private String activityCategory;
    private String activityText;

    public ActivityResponse(String activityCategory, String activityText) {
        this.activityCategory = activityCategory;
        this.activityText = activityText;
    }

    public String getActivityCategory() {
        return activityCategory;
    }

    public String getActivityText() {
        return activityText;
    }
}
