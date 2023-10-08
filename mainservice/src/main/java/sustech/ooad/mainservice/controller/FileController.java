package sustech.ooad.mainservice.controller;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sustech.ooad.mainservice.model.OssContent;
import sustech.ooad.mainservice.service.LoginUserService;
import sustech.ooad.mainservice.util.Result;
import sustech.ooad.mainservice.util.auth.AuthFunctionality;
import sustech.ooad.mainservice.util.oss.OssClient;

import java.io.IOException;
import java.util.UUID;

import static sustech.ooad.mainservice.util.ConstantField.UPLOAD_ERROR;

@Slf4j
@RestController
@RequestMapping("/content")
public class FileController {

    @Resource
    OssClient ossClient;

    @Resource
    AuthFunctionality authFunctionality;

    @Resource
    LoginUserService loginUserService;

    @PostMapping("/upload/{bucket}")
    public Result<?> uploadAvatar(@PathVariable("bucket") String bucket,
                                  @RequestParam("uri") String uri,
                                  @RequestParam("file") MultipartFile file) throws IOException {
        log.info("upload file of bytes:" + file.getSize());
        String url = ossClient.save(new OssContent(uri,bucket,file.getBytes(),file.getContentType()));
        if (StrUtil.isEmpty(url)) {
            return Result.err(UPLOAD_ERROR, "上传失败");
        } else {
            return Result.ok(url);
        }
    }


    @GetMapping("/download/**")
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        var data = ossClient.fetch(uri);
        if (data == null) {
            response.setStatus(404);
            response.getOutputStream().write(("404 not found file uri:" + uri).getBytes());
        } else {
            response.setStatus(200);
            response.setHeader("Content-Type",data.getType());
            response.getOutputStream().write(data.getData());
        }
    }
}
