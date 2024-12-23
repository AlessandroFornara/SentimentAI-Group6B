package polimi.aui.sentimentaigroup6b.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ServerResponse {

    SESSION_CREATED("Session created successfully"),
    SESSION_STARTED("Session started successfully"),
    SESSION_ENDED_ERROR("Error while ending session"),
    SESSION_ENDED("Session ended successfully"),

    SESSION_CREATION_ERROR("Error while creating session"),
    AUDIO_HANDLING_ERROR("Error while handling audio"),

    USER_REGISTRATION_SUCCESS("User registered successfully"),
    USER_REGISTRATION_ERROR("Error while registering user"),
    INVALID_CREDENTIALS("Invalid credentials");

    private final String message;
}
