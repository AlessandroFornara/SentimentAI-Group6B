package polimi.aui.sentimentaigroup6b;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import polimi.aui.sentimentaigroup6b.models.ActivityResponse;
import polimi.aui.sentimentaigroup6b.models.emotionAI.Emotion;
import polimi.aui.sentimentaigroup6b.services.SessionService;

@SpringBootTest
public class AssignActivityTest {

    @Autowired
    SessionService sessionService;

    @Test
    public void testAssignActivity() {
        Emotion[] emotionArray = Emotion.values();
        ActivityResponse activityResponse;
        for (Emotion emotion : emotionArray) {
            System.out.println(emotion);
            activityResponse = sessionService.chooseActivity(emotion);
            System.out.println(activityResponse);
        }
    }
}
