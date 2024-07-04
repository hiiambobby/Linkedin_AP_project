package com.backend.server.Controller;

import com.backend.server.Database.FollowDAO;
import com.backend.server.Model.Follow;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FollowController {
    private final FollowDAO followDAO;
    private final ObjectMapper objectMapper;

    public FollowController() {
        this.followDAO = new FollowDAO();
        this.objectMapper = new ObjectMapper();
    }

    public void createFollow(String jsonInput) throws IOException, SQLException {
        Follow follow = objectMapper.readValue(jsonInput, Follow.class);

        if (follow.getFollower() == null || follow.getFollowing() == null) {
            throw new IllegalArgumentException("Follower or following parameter is missing");
        }

        followDAO.create(follow);
    }

    public String getFollows(String userId, String type) throws SQLException, IOException {
        List<Follow> follows;

        if ("followers".equalsIgnoreCase(type)) {
            follows = followDAO.getFollowersByUserId(userId);
        } else if ("followings".equalsIgnoreCase(type)) {
            follows = followDAO.getFollowingsByUserId(userId);
        } else {
            throw new IllegalArgumentException("Invalid type parameter");
        }

        return objectMapper.writeValueAsString(follows);
    }
}