package polimi.aui.sentimentaigroup6b.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ServerResponse {

    SESSION_CREATED("Session created successfully"),
    SESSION_STARTED("Session started successfully"),
    SESSION_ENDED("Session ended successfully"),
    AUDIO_HANDLED("Audio handled successfully"),

    SESSION_CREATION_ERROR("Error while creating session"),
    SESSION_START_ERROR("Error while starting session"),
    AUDIO_HANDLING_ERROR("Error while handling audio");

    private final String message;
}
