package com.mallireddy.templategenerator.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProjectListResponse(@JsonProperty("project_id") String projectId,
                                  @JsonProperty("project_name") String projectName,
                                  @JsonProperty("created_by") String createdBy,
                                  @JsonProperty("team") String team) {
}
