package com.weibo.service;

import com.weibo.entity.Like;
import com.weibo.entity.Post;
import com.weibo.entity.User;
import com.weibo.repository.LikeRepository;
import com.weibo.repository.PostRepository;
import com.weibo.repository.UserRepository;
import com.weibo.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    public boolean toggleLike(Long postId) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(principal.getId()).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        Optional<Like> existingLike = likeRepository.findByPostIdAndUserId(postId, user.getId());
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            post.setLikeCount(post.getLikeCount() - 1);
            postRepository.save(post);
            return false;
        } else {
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            likeRepository.save(like);
            post.setLikeCount(post.getLikeCount() + 1);
            postRepository.save(post);
            return true;
        }
    }
}