package review.demo.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GitHubUrlBuilder {
    private static final String BASE_URL = "https://api.github.com";

    public String getUserRepositoriesUrl(String username) {
        return UriComponentsBuilder.fromUriString(BASE_URL)
                .path("/users/{username}/repos")
                .buildAndExpand(username)
                .toUriString();
    }

    public String getBranchesUrl(String owner, String repoName) {
        return UriComponentsBuilder.fromUriString(BASE_URL)
                .path("/repos/{owner}/{repo}/branches")
                .buildAndExpand(owner, repoName)
                .toUriString();
    }
}