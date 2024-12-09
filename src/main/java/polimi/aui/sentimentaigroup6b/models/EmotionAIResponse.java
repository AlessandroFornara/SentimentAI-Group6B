package polimi.aui.sentimentaigroup6b.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmotionAIResponse {

    private double anger;
    private double disgust;
    private double fear;
    private double joy;
    private double neutrality;
    private double sadness;
    private double surprise;
}
