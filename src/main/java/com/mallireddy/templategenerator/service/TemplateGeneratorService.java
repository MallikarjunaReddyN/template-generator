package com.mallireddy.templategenerator.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Slf4j
public class TemplateGeneratorService {

    private static final String DESTINATION_PATH = "src/main/resources/temp/projects";
    private static final String TEMPLATE_PATH = "-vcs-ref main https://github.com/MallikarjunaReddyN/copier-springboot.git ";

    public Resource generateProject(Map<String, String> inputData) {
        validateInputData(inputData);
        String cmd = buildCopierCommand(inputData);
        String projectName = inputData.get("project_name");
        String projectSlug = projectName.toLowerCase().replace(" ", "-");
        log.info("Generated command: {}", cmd);
        ProcessBuilder processBuilder = new ProcessBuilder();

        // -- Linux --
        // Run a shell command
        //processBuilder.command("bash", "-c", "ls /Users/mdireddy/Desktop/services");
        /*processBuilder.command("bash", "-c", """
                copier copy -fd 'group_id=com.arjun' -d 'project_name=Account Service' -d 'is_db_required=No' -d 'sca=No' -d 'docker=Yes' -d 'container_registery=ACR' -d 'CI=Yes' -d 'CI_type=Github Actions' -d 'aks=Yes' -d 'created_by=MalliReddyN' --vcs-ref template-restructure https://github.com/MallikarjunaReddyN/copier-springboot.git /Users/mdireddy/Desktop/services
                """);*/
        processBuilder.command("sh", "-c", cmd);

        try {
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                log.info("Project '{}' created Successfully!", projectName);
                log.info(output.toString());
            } else {
                log.error("Error occurred while generating project with exitCode {}", exitVal);
                throw new RuntimeException("Error occurred while generating project with exitCode " + exitVal);
            }

        } catch (IOException | InterruptedException e) {
            log.error("Exception occurred while generating project with exception message {}", e.getMessage());
            throw new RuntimeException("Exception occurred while generating project with exception message " + e.getMessage());
        }
        String projectCreatedPath = DESTINATION_PATH + "/" + projectSlug;
        ZipUtil.pack(new File(projectCreatedPath), new File(projectCreatedPath + ".zip"));

        Path path = Paths.get(projectCreatedPath + ".zip");
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
            FileUtils.deleteDirectory(new File(projectCreatedPath));
        } catch (Exception e) {
            log.error("Exception occurred while generating project with exception message {}", e.getMessage());
            throw new RuntimeException("Exception occurred while generating project with exception message " + e.getMessage());
        }
        return resource;
    }

    private String buildCopierCommand(Map<String, String> inputData) {
        StringBuilder cmd = new StringBuilder("copier copy -f");
        for (Map.Entry<String, String> entry : inputData.entrySet()) {
            if (StringUtils.isEmpty(entry.getValue())) {
                continue;
            }
            String key = entry.getKey();
            String value = entry.getValue();
            cmd.append("d '").append(key).append("=").append(value).append("' -");
        }
        cmd.append(TEMPLATE_PATH).append(DESTINATION_PATH);
        return cmd.toString();
    }

    private void validateInputData(Map<String, String> inputData) {
        StringBuilder errorMsg = new StringBuilder();
        String isDbRequired = inputData.get("is_db_required");
        String sca = inputData.get("sca");
        String docker = inputData.get("docker");
        String ci = inputData.get("CI");
        List<String> unwantedParams = new ArrayList<>();

        for (Map.Entry<String, String> input : inputData.entrySet()) {
            if (input.getKey().equals("server_port") || input.getKey().equals("version")
                    || input.getKey().equals("project_description") || input.getKey().equals("db_name")) {
                continue;
            }
            if ((input.getKey().equals("db") && isDbRequired.equals("No")) || (input.getKey().equals("sca_type") && sca.equals("No"))
            || (input.getKey().equals("container_registery") && docker.equals("No")) || (input.getKey().equals("CI_type") && ci.equals("No"))
            || (input.getKey().equals("aks") && docker.equals("No"))) {
                unwantedParams.add(input.getKey());
                continue;
            }
            if (StringUtils.isEmpty(input.getValue())) {
                errorMsg.append(input.getKey()).append(" must not be empty").append(",  \n");
            } else if ("group_id".equals(input.getKey()) && !Pattern.matches("^[a-z]+(\\.[a-z][a-z0-9]*)*$", input.getValue())) {
                errorMsg.append("group_id is invalid").append(", \n");
            } else if ("project_name".equals(input.getKey()) && !Pattern.matches("^[A-Za-z][A-Za-z\\s]*$", input.getValue())) {
                errorMsg.append("project_name is invalid");
            }
        }

        unwantedParams.forEach(inputData::remove);
        if (StringUtils.isNotEmpty(errorMsg)) {
            log.error("Received invalid input, error message {}", errorMsg);
            throw new RuntimeException(errorMsg.toString());
        }
    }
}
