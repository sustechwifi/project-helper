package sustech.ooad.mainservice.util.oss;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sustech.ooad.mainservice.model.OssContent;


import java.io.ByteArrayInputStream;


@Component
public class MinioOssImpl implements OssClient{

    @Value("${minio.bucket.image-bucket}")
    private String imageBucket;

    @Value("${minio.bucket.document-bucket}")
    private String documentBucket;

    @Value("${minio.bucket.video-bucket}")
    private String videoBucket;

    private final MinioClient minioClient;

    public MinioOssImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }


    private String upload(byte[] content, String path, String type,String bucketName) throws Exception{
        // 执行上传
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName) // bucket 必须传递
                .contentType(type)
                .object(path) // 相对路径作为 key
                .stream(new ByteArrayInputStream(content), content.length, -1) // 文件内容
                .build());
        return bucketName+"/"+path;
    }


    @Override
    public void save(OssContent data) {

    }

    @Override
    public OssContent fetch(String uri) {
        return null;
    }
}
