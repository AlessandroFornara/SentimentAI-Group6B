package polimi.aui.sentimentaigroup6b.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AudioEmotionsDTO {
    private double anger;
    private double disgust;
    private double fear;
    private double joy;
    private double sadness;
    private double surprise;
    private double neutrality;
}
