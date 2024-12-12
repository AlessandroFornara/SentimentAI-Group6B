package polimi.aui.sentimentaigroup6b.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ActivityResponse {

    private String activityCategory;
    private String activityText;
}
