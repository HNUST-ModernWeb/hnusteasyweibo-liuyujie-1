package com.weibo.controller;

import com.weibo.dto.ApiResponse;
import com.weibo.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {
    @Autowired
    private LikeService likeService;
    
    // 点赞/取消点赞
    @PostMapping("/post/{postId}")
    public ApiResponse<Boolean> toggleLike(@PathVariable Long postId) {
        return ApiResponse.success(likeService.toggleLike(postId));
    }
}