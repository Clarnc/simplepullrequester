package review.demo.controllers;

import org.springframework.web.bind.annotation.*;
import review.demo.dto.RepositoryResponse;
import review.demo.services.GHService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class GHController {

    private final GHService gitHubService;

    public GHController(GHService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/{username}/repositories")
    public List<RepositoryResponse> getUserRepositories(@PathVariable String username) {
        return gitHubService.getNonForkRepositories(username);
    }
}