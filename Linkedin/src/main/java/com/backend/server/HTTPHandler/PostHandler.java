package com.backend.server.HTTPHandler;

import com.backend.server.Controller.PostController;
import com.backend.server.Model.Comment;
import com.backend.server.Model.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

public class PostHandler implements HttpHandler {
    private PostController postController = new PostController();

    public PostHandler() {

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] segments = path.split("/");

        try {
            if (method.equals("GET") && segments.length == 2) {
                // Handle GET /posts
                List<Post> posts = postController.getAllPosts();
                String response = new ObjectMapper().writeValueAsString(posts);
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

            } else if (method.equals("GET") && segments.length == 3 && segments[2].equals("keyword")) {
                // Handle GET /posts/keyword?query={keyword}
                String keyword = exchange.getRequestURI().getQuery().split("=")[1]; // Extract keyword from query
                List<Post> posts = postController.getPostsByKeyword(keyword);
                String response = new ObjectMapper().writeValueAsString(posts);
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else if (method.equals("GET") && segments.length == 3 && segments[2].equals("hashtag")) {
                // Handle GET /posts/hashtag?query={hashtag}
                String hashtag = exchange.getRequestURI().getQuery().split("=")[1]; // Extract hashtag from query
                List<Post> posts = postController.getPostsByHashtag(hashtag);
                String response = new ObjectMapper().writeValueAsString(posts);
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }else if (method.equals("POST") && segments.length == 2) {InputStream is = null;
                try {
                    is = exchange.getRequestBody();
                    String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    System.out.println("Received POST request body: " + requestBody);

                    // Convert JSON string to Post object
                    ObjectMapper mapper = new ObjectMapper();
                    Post post = mapper.readValue(requestBody, Post.class);
                    System.out.println("Converted Post object: " + post.toString());

                    // Handle the post
                    postController.addPost(post);
                    exchange.sendResponseHeaders(201, -1);

                } catch (UnrecognizedPropertyException e) {
                    System.err.println("Unrecognized field in JSON: " + e.getPropertyName());
                    e.printStackTrace();
                    exchange.sendResponseHeaders(400, -1); // Bad Request
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(500, -1); // Internal Server Error
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }

                }
            }

            else if (method.equals("POST") && segments.length == 4 && segments[3].equals("like")) {
                // Handle POST /posts/{id}/like
                int postId = Integer.parseInt(segments[2]);
                String userId = new ObjectMapper().readTree(exchange.getRequestBody()).get("user_id").asText();
                postController.likePost(postId, userId);
                exchange.sendResponseHeaders(200, -1);

            } else if (method.equals("DELETE") && segments.length == 4 && segments[3].equals("like")) {
                // Handle DELETE /posts/{id}/like
                int postId = Integer.parseInt(segments[2]);
                String userId = new ObjectMapper().readTree(exchange.getRequestBody()).get("user_id").asText();
                postController.unlikePost(postId, userId);
                exchange.sendResponseHeaders(200, -1);

            } else if (method.equals("POST") && segments.length == 3 && segments[2].equals("comment")) {
                // Handle POST /posts/{id}/comment
                int postId = Integer.parseInt(segments[1]);
                InputStream is = exchange.getRequestBody();
                Comment comment = new ObjectMapper().readValue(is, Comment.class);
                comment.setPostId(postId); // Set the postId in the comment
                postController.addComment(comment);
                exchange.sendResponseHeaders(201, -1);

            } else if (method.equals("GET") && segments.length == 3 && segments[2].equals("comments")) {
                // Handle GET /posts/{id}/comments
                int postId = Integer.parseInt(segments[1]);
                List<Comment> comments = postController.getCommentsForPost(postId);
                String response = new ObjectMapper().writeValueAsString(comments);
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }

        } catch (SQLException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1); // Internal Server Error
        }
    }
}


///postman requests:

//        GET /posts: Lists all posts.
//        POST /posts: Creates a new post.
//        POST /posts/{id}/like: Likes a post.
//        DELETE /posts/{id}/like: Unlikes a post.
//        POST /posts/{id}/comment: Adds a comment to a post.
//        GET /posts/{id}/comments: Lists all comments for a post.