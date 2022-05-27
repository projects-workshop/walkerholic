package com.yunhalee.walkerholic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://172.31.0.53:3000")
            .allowedMethods(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name()
            );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        uploadFolder("profileUploads", registry);
        uploadFolder("messageUploads", registry);
        uploadFolder("productUploads", registry);
        uploadFolder("postUploads", registry);
        uploadFolder("activityUploads", registry);
    }

    private void uploadFolder(String dirName, ResourceHandlerRegistry registry) {
        Path photosDir = Paths.get(dirName);
        String photospath = photosDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + dirName + "/**")
            .addResourceLocations("file:" + photospath + "/");
    }
}