package polimi.aui.sentimentaigroup6b.models;

public class ImageResponse {
    private String name;
    private String contentType;
    private byte[] data;

    public ImageResponse(String name, String contentType, byte[] data) {
        this.name = name;
        this.contentType = contentType;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getData() {
        return data;
    }
}
