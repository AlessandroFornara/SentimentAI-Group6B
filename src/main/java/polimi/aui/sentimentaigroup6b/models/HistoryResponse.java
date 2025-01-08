package polimi.aui.sentimentaigroup6b.models;

import lombok.AllArgsConstructor;
import polimi.aui.sentimentaigroup6b.entities.BadgeType;
import polimi.aui.sentimentaigroup6b.entities.Session;

import java.util.Date;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class HistoryResponse {
    private List<SessionData> sessions;

    @AllArgsConstructor
    public class SessionData {
        private Date date;
        private int points;
        private String dominantEmotion;
        private String activityCategory;
        private String activityText;
    }
}
