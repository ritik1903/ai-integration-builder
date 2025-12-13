package com.cloudeagle.aiintegrationbuilder.controller;

import com.cloudeagle.aiintegrationbuilder.dto.User;
import com.cloudeagle.aiintegrationbuilder.entity.ApiConfig;
import com.cloudeagle.aiintegrationbuilder.repository.ApiConfigRepository;
import com.cloudeagle.aiintegrationbuilder.service.GenericApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private GenericApiService apiService;
    
    @Autowired
    private ApiConfigRepository configRepository;
    
    private final List<User> tempUsers = new CopyOnWriteArrayList<>();
    
    @PostMapping("/init-config")
    public ResponseEntity<String> initConfig() {
        try {
            configRepository.deleteAll();
            ApiConfig config = new ApiConfig(
                "calendly_users", 
                "https://api.calendly.com", 
                "/organization_memberships?organization=https://api.calendly.com/organizations/TEST_ORG", 
                "Bearer", 
                "dummy_token", 
                "{\"users\": \"resource[*].user\"}"
            );
            configRepository.save(config);
            return ResponseEntity.ok("Config created! Count: " + configRepository.count());
        } catch (Exception e) {
            return ResponseEntity.ok("Config error: " + e.getMessage());
        }
    }
    
    @PostMapping("/init-config-real")
    public ResponseEntity<String> initConfigReal(
            @RequestParam String token, 
            @RequestParam String orgUri) {
        try {
            configRepository.deleteAll();
            String endpointPath = "/organization_memberships?organization=" + orgUri;
            ApiConfig config = new ApiConfig(
                "calendly_users", 
                "https://api.calendly.com", 
                endpointPath,
                "Bearer", 
                token, 
                "{\"users\": \"resource[*].user\"}"
            );
            configRepository.save(config);
            return ResponseEntity.ok("REAL config created! Endpoint --> " + endpointPath);
        } catch (Exception e) {
            return ResponseEntity.ok("Real config error --> " + e.getMessage());
        }
    }
    
    @PostMapping("/fetch-users/{configName}")
    public ResponseEntity<?> fetchUsers(@PathVariable String configName) {
        try {
            List<User> users = apiService.fetchUsers(configName);
            tempUsers.addAll(users);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            System.err.println("Controller error: " + e.getMessage());
            return ResponseEntity.ok(new ArrayList<User>());
        }
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getStoredUsers() {
        return ResponseEntity.ok(new ArrayList<>(tempUsers));
    }
    
    @GetMapping("/configs")
    public ResponseEntity<String> testConfigs() {
        try {
            long count = configRepository.count();
            return ResponseEntity.ok("Configs ar: " + count);
        } catch (Exception e) {
            return ResponseEntity.ok("DB error: " + e.getMessage());
        }
    }
}
