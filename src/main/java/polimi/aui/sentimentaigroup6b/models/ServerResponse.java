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
    AUDIO_HANDLING_ERROR("Error while handling audio"),

    USER_REGISTRATION_SUCCESS("User registered successfully"),
    USER_REGISTRATION_ERROR("Error while registering user"),
    USER_LOGGED_IN("User logged in successfully"),
    INVALID_CREDENTIALS("Invalid credentials");

    private final String message;
}
