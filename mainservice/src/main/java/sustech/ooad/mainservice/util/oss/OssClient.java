package sustech.ooad.mainservice.util.oss;

import sustech.ooad.mainservice.model.OssContent;

public interface OssClient {

    void save(OssContent data);

    OssContent fetch(String uri);

}
