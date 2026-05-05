package com.weibo.controller;

import com.weibo.dto.ApiResponse;
import com.weibo.dto.PostRequest;
import com.weibo.entity.Post;
import com.weibo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public ApiResponse<Page<Post>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(postService.getAllPosts(page, size));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<Page<Post>> getUserPosts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(postService.getUserPosts(userId, page, size));
    }

    @PostMapping
    public ApiResponse<Post> createPost(@RequestBody PostRequest request) {
        return ApiResponse.success(postService.createPost(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<Post> getPost(@PathVariable Long id) {
        return ApiResponse.success(postService.getPostById(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ApiResponse.success("Post deleted");
    }
}