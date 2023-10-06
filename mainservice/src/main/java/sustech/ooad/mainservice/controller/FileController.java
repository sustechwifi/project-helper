package sustech.ooad.mainservice.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sustech.ooad.mainservice.util.Result;
import sustech.ooad.mainservice.util.oss.OssClient;

@Slf4j
@RestController
@RequestMapping("/content")
public class FileController {

    @Resource
    OssClient ossClient;

    @PostMapping("/upload")
    public Result<String> uploadFile(@RequestBody MultipartFile file, HttpServletRequest request) throws Exception {


        //return Result.ok(fileService.createFile(file.getOriginalFilename(), path, IoUtil.readBytes(file.getInputStream())));
        return null;
    }


    @GetMapping("/avatar/{uid}")
    public void getAvatar(@PathVariable("uid") long uid, HttpServletResponse response){

    }
}
