package review.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import review.demo.dto.RepositoryResponse;
import review.demo.services.GHService;
import review.demo.utils.GitHubUrlBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GHServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GitHubUrlBuilder urlBuilder;

    @InjectMocks
    private GHService ghService;

    @Test
    void getNonForkRepositories_shouldReturnRepositories() {

        String username = "testuser";
        String repoUrl = "https://api.github.com/users/testuser/repos";
        String branchUrl = "https://api.github.com/repos/testuser/repo1/branches";


        when(urlBuilder.getUserRepositoriesUrl(username)).thenReturn(repoUrl);
        when(urlBuilder.getBranchesUrl(anyString(), anyString())).thenReturn(branchUrl);

        GHService.GitHubRepository mockRepo = new GHService.GitHubRepository();
        mockRepo.name = "repo1";
        mockRepo.fork = false;
        mockRepo.owner = new GHService.GitHubOwner();
        mockRepo.owner.login = "testuser";

        GHService.GitHubBranch mockBranch = new GHService.GitHubBranch();
        mockBranch.name = "main";
        mockBranch.commit = new GHService.GitHubCommit();
        mockBranch.commit.sha = "sha123";

        when(restTemplate.getForObject(repoUrl, GHService.GitHubRepository[].class))
                .thenReturn(new GHService.GitHubRepository[]{mockRepo});

        when(restTemplate.getForObject(branchUrl, GHService.GitHubBranch[].class))
                .thenReturn(new GHService.GitHubBranch[]{mockBranch});


        List<RepositoryResponse> result = ghService.getNonForkRepositories(username);


        assertEquals(1, result.size());
        assertEquals("repo1", result.getFirst().repositoryName());
        assertEquals(1, result.getFirst().branches().size());
    }
}