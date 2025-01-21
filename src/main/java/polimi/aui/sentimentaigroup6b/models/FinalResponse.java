package polimi.aui.sentimentaigroup6b.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import polimi.aui.sentimentaigroup6b.entities.BadgeType;

import java.util.Map;

@AllArgsConstructor
@Getter
@ToString
public class FinalResponse {

    private String dominantEmotion;
    private ActivityResponse activityResponse;
    private Map<BadgeType, Integer> badges;
    private int points;
    private int level;
}
