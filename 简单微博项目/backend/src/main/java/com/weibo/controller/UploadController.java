package com.weibo.controller;

import com.weibo.dto.ApiResponse;
import com.weibo.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class UploadController {
    @Autowired
    private FileStorageService fileStorageService;
    
    @PostMapping("/image")
    public ApiResponse<?> uploadImage(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileStorageService.storeFile(file);
        Map<String, String> data = new HashMap<>();
        data.put("url", fileUrl);
        return ApiResponse.success(data);
    }
}