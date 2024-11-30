package polimi.aui.sentimentaigroup6b.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import polimi.aui.sentimentaigroup6b.utils.PythonRunner;

@Service
@AllArgsConstructor
public class SessionService {

    private final BadgeService badgeService;
    private final PythonRunner runner;

    public void sendEmotionDetectionRequest(String audio){
        runner.runPythonFunction(audio);
    }
}
