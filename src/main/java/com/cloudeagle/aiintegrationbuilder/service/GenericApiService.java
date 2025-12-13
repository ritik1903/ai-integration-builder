package com.cloudeagle.aiintegrationbuilder.service;

import com.cloudeagle.aiintegrationbuilder.dto.User;
import com.cloudeagle.aiintegrationbuilder.entity.ApiConfig;
import com.cloudeagle.aiintegrationbuilder.repository.ApiConfigRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GenericApiService {
    
    @Autowired
    private ApiConfigRepository configRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public List<User> fetchUsers(String configName) {
        try {
            Optional<ApiConfig> configOpt = configRepository.findByName(configName);
            if (configOpt.isEmpty()) {
                System.out.println("Config not found: " + configName);
                return new ArrayList<>();
            }
            
            ApiConfig config = configOpt.get();
            String url = config.getBaseUrl() + config.getEndpointPath();
            
            System.out.println("Calling the URL: " + url);
            System.out.println("Here is the Token preview: " + config.getAuthToken().substring(0, 20) + "...");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(config.getAuthToken());
            headers.set("Accept", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            
            System.out.println("API Status: " + response.getStatusCode());
            System.out.println("API Response preview: " + response.getBody().substring(0, Math.min(300, response.getBody().length())));

            return parseCalendlyUsers(response.getBody());
            
        } catch (Exception e) {
            System.err.println("Service error: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    private List<User> parseCalendlyUsers(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode resources = root.path("resource");
            
            List<User> users = new ArrayList<>();
            if (resources.isArray()) {
                for (JsonNode resource : resources) {
                    JsonNode userNode = resource.path("user");
                    if (!userNode.isMissingNode()) {
                        User user = new User();
                        user.setId(userNode.path("uri").asText());
                        user.setName(userNode.path("name").asText("Unknown"));
                        user.setEmail(userNode.path("email").asText("N/A"));
                        users.add(user);
                    }
                }
            }
            System.out.println("Parsed " + users.size() + " users");
            return users;
        } catch (Exception e) {
            System.err.println("Parse error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
