package com.mallireddy.templategenerator.controller;

import com.mallireddy.templategenerator.domain.response.ApiResponse;
import com.mallireddy.templategenerator.domain.response.ProjectListResponse;
import com.mallireddy.templategenerator.domain.response.TemplateGeneratorStatusResponse;
import com.mallireddy.templategenerator.service.TemplateGeneratorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/templategenerator")
@Tag(name = "TemplateGeneratorController", description = "TemplateGenerator management APIs")
public class TemplateGeneratorController {

    @Autowired
    private TemplateGeneratorService templategeneratorService;

    @GetMapping("/status")
    public ApiResponse<TemplateGeneratorStatusResponse> showStatus() {
        TemplateGeneratorStatusResponse templategeneratorStatusResponse = new TemplateGeneratorStatusResponse("UP", "template-generator", "MalliReddyN");
        return ApiResponse.success("200", ApiResponse.Status.SUCCESS.toString(), templategeneratorStatusResponse);
    }

    @PostMapping("/generate-project")
    public ApiResponse<Resource> generateProject(HttpServletResponse response, @RequestBody Map<String, String> inputData, @RequestParam("team") String team) {
        Resource resource = templategeneratorService.generateProject(inputData, team);
        response.setContentType("application/octet-stream");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
        return ApiResponse.ok(resource);
    }

    @GetMapping("/project-list")
    public ApiResponse<List<ProjectListResponse>> projectList() {
        List<ProjectListResponse> projectList = templategeneratorService.getProjectList();
        return ApiResponse.ok(projectList);
    }

}
