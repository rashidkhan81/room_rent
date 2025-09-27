package com.room_rent.Room_Rent_Application.config.fileConfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.file")
@Getter
@Setter
public class FileStorageConfig {
    private String uploadDir;
}
