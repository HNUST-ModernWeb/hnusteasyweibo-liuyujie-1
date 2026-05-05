package com.weibo.service;

import com.weibo.dto.PostRequest;
import com.weibo.entity.Post;
import com.weibo.entity.User;
import com.weibo.repository.LikeRepository;
import com.weibo.repository.PostRepository;
import com.weibo.repository.UserRepository;
import com.weibo.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikeRepository likeRepository;   // 新增注入
    
    public Post createPost(PostRequest request) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(principal.getId()).orElseThrow();
        Post post = new Post();
        post.setContent(request.getContent());
        post.setImages(request.getImages());
        post.setUser(user);
        post.setLikeCount(0);
        return postRepository.save(post);
    }
    
    public Page<Post> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        // 设置当前用户是否点赞
        Long currentUserId = getCurrentUserId();
        posts.forEach(post -> {
            if (currentUserId != null) {
                post.setLikedByCurrentUser(likeRepository.existsByPostIdAndUserId(post.getId(), currentUserId));
            } else {
                post.setLikedByCurrentUser(false);
            }
        });
        return posts;
    }
    
    public Page<Post> getUserPosts(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        Long currentUserId = getCurrentUserId();
        posts.forEach(post -> {
            if (currentUserId != null) {
                post.setLikedByCurrentUser(likeRepository.existsByPostIdAndUserId(post.getId(), currentUserId));
            } else {
                post.setLikedByCurrentUser(false);
            }
        });
        return posts;
    }
    
    public Post getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        Long currentUserId = getCurrentUserId();
        if (currentUserId != null) {
            post.setLikedByCurrentUser(likeRepository.existsByPostIdAndUserId(post.getId(), currentUserId));
        } else {
            post.setLikedByCurrentUser(false);
        }
        return post;
    }
    
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
    
    // 获取当前登录用户ID，未登录返回null
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal) {
            return ((UserPrincipal) auth.getPrincipal()).getId();
        }
        return null;
    }
}