package polimi.aui.sentimentaigroup6b.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import polimi.aui.sentimentaigroup6b.utils.PythonRunner;

@Service
@AllArgsConstructor
public class SessionService {

    private final BadgeService badgeService;
    private final PythonRunner runner;

    public void createSession() {
        /*
        IN PARALLELO:
            Crea oggetto nel database
            Chiamata a AI per generazione immagini

        Restituisce all'utente le immagini generate
         */
    }

    public void startSession(){
        /*
        Salva timestamp nel database di inizio sessione e salva immagine
        Manda ok per iniziare a registrare audio lato frontend
         */
    }

    public void handleAudio(String audio) {
        sendEmotionDetectionRequest(audio);
        /*

         */
    }

    public void endSession(){
        /*
        Salva timestamp nel database di fine sessione
        Calcola badge, activities e restituisce all'utente
         */
    }

    //TODO: Cambiare String a byte[]
    public void sendEmotionDetectionRequest(String audio){
        runner.runPythonFunction(audio);
    }
}
