package com.mallireddy.templategenerator.controller;

import com.mallireddy.templategenerator.domain.response.ApiResponse;
import com.mallireddy.templategenerator.domain.response.TemplateGeneratorStatusResponse;
import com.mallireddy.templategenerator.service.TemplateGeneratorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.zeroturnaround.zip.commons.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
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
    public ApiResponse<Void> generateProject(HttpServletResponse response, @RequestBody Map<String, String> inputData, @RequestParam("team") String team) throws IOException {
        Resource resource = templategeneratorService.generateProject(inputData, team);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
        response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()));
        FileUtils.copy(resource.getFile(), response.getOutputStream());
        response.flushBuffer();
        Files.deleteIfExists(resource.getFile().toPath());
        return ApiResponse.ok();
    }

}
