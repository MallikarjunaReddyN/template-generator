package com.mallireddy.templategenerator.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TemplateGeneratorStatusResponse(String status, @JsonProperty("service_name") String serviceName, @JsonProperty("created_by") String createdBy) {
}
