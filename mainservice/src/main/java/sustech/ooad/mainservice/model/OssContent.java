package sustech.ooad.mainservice.model;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public class OssContent {
    private MultipartFile originalFile;
    private String type;
    private String fileName;
    private byte[] data;
    private long expiration;
    private String uri;
    private String url;

    public OssContent(MultipartFile originalFile, HttpServletRequest request) {
        this.originalFile = originalFile;
    }


}
