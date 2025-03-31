package review.demo.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;


public record RepositoryResponse(
        @JsonProperty("repositoryName") String repositoryName,
        @JsonProperty("ownerLogin") String ownerLogin,
        @JsonProperty("branches") List<BranchResponse> branches
) {}
