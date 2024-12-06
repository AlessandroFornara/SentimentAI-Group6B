package polimi.aui.sentimentaigroup6b.models;

public enum ServerResponse {

    SESSION_CREATED("Sessione creata con successo"),
    SESSION_STARTED("Sessione iniziata con successo"),
    SESSION_ENDED("Sessione terminata con successo"),
    AUDIO_HANDLED("Audio gestito con successo"),
    SESSION_CREATION_ERROR("Error while creating session");

    private final String message;

    ServerResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
