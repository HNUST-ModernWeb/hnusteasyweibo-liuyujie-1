package com.weibo.controller;

import com.weibo.dto.ApiResponse;
import com.weibo.dto.CommentRequest;
import com.weibo.entity.Comment;
import com.weibo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    
    @PostMapping("/post/{postId}")
    public ApiResponse<Comment> addComment(@PathVariable Long postId, @RequestBody CommentRequest request) {
        return ApiResponse.success(commentService.addComment(postId, request));
    }
    
    @GetMapping("/post/{postId}")
    public ApiResponse<List<Comment>> getComments(@PathVariable Long postId) {
        return ApiResponse.success(commentService.getCommentsByPost(postId));
    }
}