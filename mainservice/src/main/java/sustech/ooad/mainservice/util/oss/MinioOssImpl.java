package sustech.ooad.mainservice.util.oss;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sustech.ooad.mainservice.model.OssContent;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@Slf4j
@Component
public class MinioOssImpl implements OssClient {


    @Value("${minio.endpoint}")
    String endpoint;

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


    private String upload(byte[] content, String path, String type, String bucketName) throws Exception {
        // 执行上传
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName) // bucket 必须传递
                .contentType(type)
                .object(path)
                .stream(new ByteArrayInputStream(content), content.length, -1) // 文件内容
                .build());
        return String.format("%s/download/%s/%s",endpoint,bucketName,path);
    }



    @Override
    public String save(OssContent data) {
        try {
            return upload(data.getData(), data.getUri(), data.getType(), data.getBucket());
        } catch (Exception e) {
            log.error("error when save "+data.getUri()+" to bucket "+data.getBucket());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public OssContent fetch(String uri) {
        String bucket = uri.split("/")[3];
        String path = uri.substring(String.format("/%s/download/%s","content",bucket).length());
        try {
            OssContent content = new OssContent();
            var resp = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(path)
                    .build());
            content.setType(resp.headers().get("Content-Type"));
            content.setData(resp.readAllBytes());
            return content;
        } catch (Exception e) {
            log.error("error when fetch "+path+" to bucket "+bucket);
            e.printStackTrace();
            return null;
        }
    }
}
