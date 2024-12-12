package polimi.aui.sentimentaigroup6b.models;

import lombok.AllArgsConstructor;
import polimi.aui.sentimentaigroup6b.entities.Badge;

import java.util.List;

@AllArgsConstructor
public class FinalResponse {

    private String dominantEmotion;

    private ActivityResponse activityResponse;

    private List<Badge> badges;
}
