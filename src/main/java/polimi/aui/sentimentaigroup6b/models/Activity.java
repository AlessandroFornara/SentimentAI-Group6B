package polimi.aui.sentimentaigroup6b.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import polimi.aui.sentimentaigroup6b.models.emotionAI.Emotion;

@AllArgsConstructor
@Getter
public enum Activity {
    PERSONALIZED_QUOTE("Personalized Quote"),
    SELF_CARE_SUGGESTION("Self-Care Suggestion"),
    CREATIVE_MINI_ACTIVITY("Creative Mini Activity"),
    SHORT_GUIDED_MEDITATION("Short Guided Meditation"),
    CHALLENGE("Challenge"),
    RECOMMENDED_MUSIC("Recommended Music"),;

    private final String categoryDescription;

    public String assignActivity(Emotion detectedEmotion, String language) {
        if(language.equals("en-US"))
            return assignActivityEN(detectedEmotion);
        else
            return assignActivityIT(detectedEmotion);
    }

    private String assignActivityEN(Emotion detectedEmotion) {
        return switch (detectedEmotion) {
            case JOY -> switch (this) {
                case PERSONALIZED_QUOTE -> "Happiness is not a destination, but a journey. Enjoy every step!";
                case SELF_CARE_SUGGESTION -> "You are feeling joyful! Take some time to enjoy your favorite activity today.";
                case CREATIVE_MINI_ACTIVITY -> "Take a photo of something that represents your happiness and save it as a memory.";
                case SHORT_GUIDED_MEDITATION -> "You are feeling joyful! Here is a short guided meditation to keep you relaxed.";
                case CHALLENGE -> "Today, give someone a sincere compliment or thank a person who made your day better.";
                case RECOMMENDED_MUSIC -> "A playlist with cheerful songs to extend your good mood.";
            };
            case SADNESS -> switch (this) {
                case PERSONALIZED_QUOTE -> "Sadness is a part of life, but also an opportunity to rediscover what truly matters.";
                case SELF_CARE_SUGGESTION -> "Today, take 15 minutes to listen to your favorite music or read a chapter of your favorite book.";
                case CREATIVE_MINI_ACTIVITY -> "Write a paragraph about a happy moment that makes you feel grateful, even when you’re feeling sad.";
                case SHORT_GUIDED_MEDITATION -> "A short 3–5 minute meditation to ease sadness and regain some peace of mind.";
                case CHALLENGE -> "Today, write down three things you’re grateful for. Focusing on the good in your life can help improve your mood.";
                case RECOMMENDED_MUSIC -> "A playlist with calming tracks to encourage relaxation.";
            };
            case ANGER -> switch (this) {
                case PERSONALIZED_QUOTE -> "Calmness is the power to stay balanced even when everything around you is in chaos.";
                case SELF_CARE_SUGGESTION -> "Take 5 minutes to step away from what made you angry and focus on an activity that relaxes you.";
                case CREATIVE_MINI_ACTIVITY -> "Write down what made you angry today and try to identify a positive aspect or a lesson to learn.";
                case SHORT_GUIDED_MEDITATION -> "A targeted meditation to calm anger and refocus the mind.";
                case CHALLENGE -> "Find a small positive thing today and focus on it, even if it’s simple.";
                case RECOMMENDED_MUSIC -> "A playlist with calming music to ease tension and help you relax.";
            };
            case DISGUST -> switch (this) {
                case PERSONALIZED_QUOTE -> "Acceptance is the key to inner peace.";
                case SELF_CARE_SUGGESTION -> "Spend a few minutes on a pleasant activity, like listening to your favorite music or reading something interesting.";
                case CREATIVE_MINI_ACTIVITY -> "Visualize a place or situation that brings peace and try to draw or write about it.";
                case SHORT_GUIDED_MEDITATION -> "A meditation to help distance yourself from feelings of disgust or discomfort.";
                case CHALLENGE -> "Find one small positive thing about today and focus on it.";
                case RECOMMENDED_MUSIC -> "A playlist with soothing tracks to help you find balance and relaxation.";
            };
            case FEAR -> switch (this) {
                case PERSONALIZED_QUOTE -> "Courage is not the absence of fear, but the decision to move forward anyway.";
                case SELF_CARE_SUGGESTION -> "Take a moment to list the things that make you feel safe and protected.";
                case CREATIVE_MINI_ACTIVITY -> "Write about a time when you overcame a difficult situation to remind yourself of your strength.";
                case SHORT_GUIDED_MEDITATION -> "A guided meditation to calm the mind and reduce anxiety.";
                case CHALLENGE -> "Set a small, achievable goal today to boost your confidence.";
                case RECOMMENDED_MUSIC -> "A playlist with soothing and calming tracks to ease the mind.";
            };
            case NEUTRALITY -> switch (this) {
                case PERSONALIZED_QUOTE -> "Serenity is an inner achievement. Find beauty in the simplicity of your day.";
                case SELF_CARE_SUGGESTION -> "Spend a few minutes on a small pleasurable activity, like reading a chapter of a book or listening to a podcast.";
                case CREATIVE_MINI_ACTIVITY -> "Think of one thing that makes you happy or satisfied and write it down to remember it.";
                case SHORT_GUIDED_MEDITATION -> "A short meditation to encourage relaxation and mindfulness.";
                case CHALLENGE -> "Write down one small positive thing you noticed today, even if it’s simple.";
                case RECOMMENDED_MUSIC -> "A soft playlist to encourage reflection or relaxation.";
            };
            case SURPRISE -> switch (this) {
                case PERSONALIZED_QUOTE -> "Life is full of surprises. Embrace the unexpected!";
                case SELF_CARE_SUGGESTION -> "Take a moment to enjoy the surprise and reflect on what it means to you.";
                case CREATIVE_MINI_ACTIVITY -> "Capture the surprise in a drawing or a short story.";
                case SHORT_GUIDED_MEDITATION -> "A meditation to help you process and enjoy the surprise.";
                case CHALLENGE -> "Share the surprise with a friend or family member and see their reaction.";
                case RECOMMENDED_MUSIC -> "A playlist with upbeat and surprising tracks to match your mood.";
            };
            default -> throw new IllegalStateException("Unexpected emotion value: " + this);
        };
    }

    private String assignActivityIT(Emotion detectedEmotion) {
        return switch (detectedEmotion) {
            case JOY -> switch (this) {
                case PERSONALIZED_QUOTE -> "La felicità non è una destinazione, ma un viaggio. Goditi ogni passo!";
                case SELF_CARE_SUGGESTION -> "Ti senti felice! Concediti del tempo per svolgere la tua attività preferita.";
                case CREATIVE_MINI_ACTIVITY -> "Scatta una foto di qualcosa che rappresenta la tua felicità e conservala come ricordo.";
                case SHORT_GUIDED_MEDITATION -> "Ti senti felice! Ecco una breve meditazione guidata per mantenerti rilassato.";
                case CHALLENGE -> "Fai un complimento sincero a qualcuno o ringrazia una persona che ha migliorato la tua giornata.";
                case RECOMMENDED_MUSIC -> "Una playlist con canzoni allegre per prolungare il tuo buon umore.";
            };
            case SADNESS -> switch (this) {
                case PERSONALIZED_QUOTE -> "La tristezza fa parte della vita, ma è anche un'opportunità per riscoprire ciò che conta davvero.";
                case SELF_CARE_SUGGESTION -> "Dedica 15 minuti ad ascoltare la tua musica preferita o a leggere un capitolo del tuo libro preferito.";
                case CREATIVE_MINI_ACTIVITY -> "Scrivi un paragrafo su un momento felice che ti fa sentire grato, anche quando ti senti triste.";
                case SHORT_GUIDED_MEDITATION -> "Una breve meditazione di 3–5 minuti per alleviare la tristezza e ritrovare un po' di serenità.";
                case CHALLENGE -> "Scrivi tre cose per cui sei grato. Concentrarti sul positivo può migliorare il tuo umore.";
                case RECOMMENDED_MUSIC -> "Una playlist con tracce rilassanti per incoraggiare il rilassamento.";
            };
            case ANGER -> switch (this) {
                case PERSONALIZED_QUOTE -> "La calma è il potere di rimanere equilibrati anche quando tutto intorno c'è caos.";
                case SELF_CARE_SUGGESTION -> "Prenditi 5 minuti per allontanarti da ciò che ti ha fatto arrabbiare e concentrarti su un'attività che ti rilassa.";
                case CREATIVE_MINI_ACTIVITY -> "Scrivi ciò che ti ha fatto arrabbiare oggi e prova a individuare un aspetto positivo o una lezione da imparare.";
                case SHORT_GUIDED_MEDITATION -> "Una meditazione mirata per calmare la rabbia e rifocalizzare la mente.";
                case CHALLENGE -> "Trova una piccola cosa positiva oggi e concentrati su di essa, anche se semplice.";
                case RECOMMENDED_MUSIC -> "Una playlist con musica rilassante per alleviare la tensione e aiutarti a rilassarti.";
            };
            case DISGUST -> switch (this) {
                case PERSONALIZED_QUOTE -> "L'accettazione è la chiave per la pace interiore.";
                case SELF_CARE_SUGGESTION -> "Dedica qualche minuto a un'attività piacevole, come ascoltare la tua musica preferita o leggere qualcosa di interessante.";
                case CREATIVE_MINI_ACTIVITY -> "Visualizza un luogo o una situazione che ti trasmette pace e prova a disegnarla o a scriverne.";
                case SHORT_GUIDED_MEDITATION -> "Una meditazione per distanziarti dai sentimenti di disgusto o disagio.";
                case CHALLENGE -> "Trova una piccola cosa positiva di oggi e concentrati su di essa.";
                case RECOMMENDED_MUSIC -> "Una playlist con tracce rilassanti per aiutarti a trovare equilibrio e relax.";
            };
            case FEAR -> switch (this) {
                case PERSONALIZED_QUOTE -> "Il coraggio non è l'assenza di paura, ma la decisione di andare avanti comunque.";
                case SELF_CARE_SUGGESTION -> "Prenditi un momento per elencare le cose che ti fanno sentire al sicuro e protetto.";
                case CREATIVE_MINI_ACTIVITY -> "Scrivi di una volta in cui hai superato una situazione difficile per ricordarti della tua forza.";
                case SHORT_GUIDED_MEDITATION -> "Una meditazione guidata per calmare la mente e ridurre l'ansia.";
                case CHALLENGE -> "Stabilisci un piccolo obiettivo raggiungibile oggi per aumentare la tua fiducia.";
                case RECOMMENDED_MUSIC -> "Una playlist con tracce rilassanti e calmanti per tranquillizzare la mente.";
            };
            case NEUTRALITY -> switch (this) {
                case PERSONALIZED_QUOTE -> "La serenità è una conquista interiore. Trova la bellezza nella semplicità della tua giornata.";
                case SELF_CARE_SUGGESTION -> "Dedica qualche minuto a una piccola attività piacevole, come leggere un capitolo di un libro o ascoltare un podcast.";
                case CREATIVE_MINI_ACTIVITY -> "Pensa a una cosa che ti rende felice o soddisfatto e annotala per ricordarla.";
                case SHORT_GUIDED_MEDITATION -> "Una breve meditazione per incoraggiare il rilassamento e la consapevolezza.";
                case CHALLENGE -> "Scrivi una piccola cosa positiva che hai notato oggi, anche se è semplice.";
                case RECOMMENDED_MUSIC -> "Una playlist delicata per incoraggiare la riflessione o il rilassamento.";
            };
            case SURPRISE -> switch (this) {
                case PERSONALIZED_QUOTE -> "La vita è piena di sorprese. Accogli l'inaspettato!";
                case SELF_CARE_SUGGESTION -> "Prenditi un momento per goderti la sorpresa e riflettere su cosa significa per te.";
                case CREATIVE_MINI_ACTIVITY -> "Cattura la sorpresa in un disegno o in una breve storia.";
                case SHORT_GUIDED_MEDITATION -> "Una meditazione per aiutarti a elaborare e goderti la sorpresa.";
                case CHALLENGE -> "Condividi la sorpresa con un amico o un familiare e osserva la loro reazione.";
                case RECOMMENDED_MUSIC -> "Una playlist con brani vivaci e sorprendenti per adattarsi al tuo umore.";
            };
            default -> throw new IllegalStateException("Valore emozione inatteso: " + this);
        };
    }
}
