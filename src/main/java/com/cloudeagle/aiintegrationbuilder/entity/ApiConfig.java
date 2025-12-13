package com.cloudeagle.aiintegrationbuilder.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_configs")
public class ApiConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "base_url")
    private String baseUrl;
    
    @Column(name = "endpoint_path")
    private String endpointPath;
    
    @Column(name = "auth_type")
    private String authType;
    
    @Column(name = "auth_token", length = 2000)
    private String authToken;
    
    @Column(name = "response_mapping", columnDefinition = "TEXT")
    private String responseMapping;
    
    // here is the  Constructors, getters, setters...,.
    public ApiConfig() {}
    
    public ApiConfig(String name, String baseUrl, String endpointPath, 
                    String authType, String authToken, String responseMapping) {
        this.name = name;
        this.baseUrl = baseUrl;
        this.endpointPath = endpointPath;
        this.authType = authType;
        this.authToken = authToken;
        this.responseMapping = responseMapping;
    }
    
    // here is theGetters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    
    public String getEndpointPath() { return endpointPath; }
    public void setEndpointPath(String endpointPath) { this.endpointPath = endpointPath; }
    
    public String getAuthType() { return authType; }
    public void setAuthType(String authType) { this.authType = authType; }
    
    public String getAuthToken() { return authToken; }
    public void setAuthToken(String authToken) { this.authToken = authToken; }
    
    public String getResponseMapping() { return responseMapping; }
    public void setResponseMapping(String responseMapping) { this.responseMapping = responseMapping; }
}
