package review.demo.services;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import review.demo.dto.BranchResponse;
import review.demo.dto.RepositoryResponse;
import review.demo.exceptions.UserNotFoundException;
import review.demo.utils.GitHubUrlBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service

public class GHService {
    private final RestTemplate restTemplate;
    private final GitHubUrlBuilder urlBuilder;


    public GHService(RestTemplate restTemplate, GitHubUrlBuilder urlBuilder) {
        this.restTemplate = restTemplate;
        this.urlBuilder = urlBuilder;
    }

    public List<RepositoryResponse> getNonForkRepositories(String username) {

        try {
            String reposUrl = urlBuilder.getUserRepositoriesUrl(username);
            GitHubRepository[] repos = restTemplate.getForObject(reposUrl, GitHubRepository[].class);

            if (repos == null || repos.length == 0) {
                throw new UserNotFoundException("GitHub user not found");
            }

            return Arrays.stream(repos)
                    .filter(Objects::nonNull)
                    .filter(repo -> !repo.isFork())
                    .map(repo -> {
                        GitHubBranch[] branches = restTemplate.getForObject(
                                urlBuilder.getBranchesUrl(username, repo.getName()),
                                GitHubBranch[].class
                        );

                        return new RepositoryResponse(
                                repo.getName(),
                                repo.getOwner().getLogin(),
                                branches != null ?
                                        Arrays.stream(branches)
                                                .filter(Objects::nonNull)
                                                .map(b -> new BranchResponse(b.getName(), b.getCommit().getSha()))
                                                .toList()
                                        : List.of()
                        );
                    })
                    .toList();

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException("GitHub user not found");
            }
            throw new RuntimeException("GitHub API error");
        }
    }


    @Getter
    public static class GitHubRepository {

        public String name;
        public boolean fork;
        public GitHubOwner owner;


    }

    @Getter
    public static class GitHubOwner {
         public String login;
    }

    @Getter
    public static class GitHubBranch {
        public String name;
        public GitHubCommit commit;

    }

    @Getter
    public static class GitHubCommit {
        public String sha;
    }
}