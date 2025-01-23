package polimi.aui.sentimentaigroup6b.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class HistoryResponse {
    private List<SessionData> sessions;

    @AllArgsConstructor
    @Getter
    @ToString
    public static class SessionData {
        private Date date;
        private String topic;
        private String dominantEmotion;
        private String activityCategory;
        private String activityText;
    }
}
