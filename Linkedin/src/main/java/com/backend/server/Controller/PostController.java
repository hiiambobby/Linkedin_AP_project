package com.backend.server.Controller;


import com.backend.server.Database.CommentDAO;
import com.backend.server.Database.LikeDAO;
import com.backend.server.Database.PostDAO;
import com.backend.server.Model.Comment;
import com.backend.server.Model.Post;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;
import java.util.List;

public class PostController {
    private PostDAO postDAO;
    private LikeDAO likesDAO;
    private CommentDAO commentDAO;

    public PostController() {
        this.postDAO = new PostDAO();
        this.likesDAO = new LikeDAO();
        this.commentDAO = new CommentDAO();
    }

    public List<Post> getAllPosts() throws SQLException, JsonProcessingException {
        return postDAO.getAllPosts();
    }

    public void addPost(Post post) throws SQLException {
        postDAO.insertPost(post);
    }

    public void likePost(int postId, String userId) throws SQLException {
        likesDAO.addLike(postId, userId);
    }

    public void unlikePost(int postId, String userId) throws SQLException {
        likesDAO.removeLike(postId, userId);
    }

    public void addComment(Comment comment) throws SQLException {
        commentDAO.insertComment(comment);
    }

    // Get all comments for a specific post
    public List<Comment> getCommentsForPost(int postId) throws SQLException {
        return commentDAO.getCommentsByPostId(postId);
    }
    public List<Post> getPostsByKeyword(String keyword) throws SQLException {
        return postDAO.getPostsByKeyword(keyword);
    }
    public List<Post> getPostsByHashtag(String hashtag) throws SQLException {
        return postDAO.getPostsByHashtag(hashtag);
    }

}
