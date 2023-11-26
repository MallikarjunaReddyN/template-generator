package com.mallireddy.templategenerator.controller;

import com.mallireddy.templategenerator.domain.response.ApiResponse;
import com.mallireddy.templategenerator.domain.response.TemplateGeneratorStatusResponse;
import com.mallireddy.templategenerator.service.TemplateGeneratorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> generateProject(HttpServletResponse response, @RequestBody Map<String, String> inputData) {
        try {
            Resource resource = templategeneratorService.generateProject(inputData);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()));
            FileUtils.copy(resource.getFile(), response.getOutputStream());
            response.flushBuffer();
            Files.deleteIfExists(resource.getFile().toPath());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
