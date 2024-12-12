package polimi.aui.sentimentaigroup6b.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ImageResponse {
    private String name;
    private String contentType;
    private byte[] data;
}
