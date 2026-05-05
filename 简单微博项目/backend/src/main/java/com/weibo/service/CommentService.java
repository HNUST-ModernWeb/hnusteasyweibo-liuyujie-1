package com.weibo.service;

import com.weibo.dto.CommentRequest;
import com.weibo.entity.Comment;
import com.weibo.entity.Post;
import com.weibo.entity.User;
import com.weibo.repository.CommentRepository;
import com.weibo.repository.PostRepository;
import com.weibo.repository.UserRepository;
import com.weibo.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    
    public Comment addComment(Long postId, CommentRequest request) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(principal.getId()).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setUser(user);
        comment.setPost(post);
        return commentRepository.save(comment);
    }
    
    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
    }
}