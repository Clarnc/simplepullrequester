package review.demo.dto;



import com.fasterxml.jackson.annotation.JsonProperty;

public record BranchResponse(
        @JsonProperty("name") String name,
        @JsonProperty("lastCommitSha") String lastCommitSha
) {}