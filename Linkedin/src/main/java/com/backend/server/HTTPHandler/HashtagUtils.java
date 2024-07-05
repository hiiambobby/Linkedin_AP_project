package com.backend.server.HTTPHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashtagUtils {
    public static Set<String> extractHashtags(String text) {
        Set<String> hashtags = new HashSet<>();
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            hashtags.add(matcher.group().toLowerCase()); // Store hashtags in lowercase
        }

        return hashtags;
    }
}
