package com.yorozuya.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Ballauma
 */
@Component
@ConfigurationProperties(prefix = "yorozuya.deepseek")
@Data
public class DeepSeekProperties {
    private String apiKey;
    private String baseUrl;
    private String model;
}