package com.yunhalee.walkerholic.common.controller;

import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.user.domain.Level;
import com.yunhalee.walkerholic.user.dto.LevelDTO;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/levels")
    public List<LevelDTO> getLevels() {
        List<LevelDTO> levels = Arrays.stream(Level.values()).map(level -> new LevelDTO(level))
            .collect(Collectors.toList());
        return levels;
    }

    @GetMapping("/categories")
    public List<String> getCategories() {
        return Arrays.stream(Category.values()).map(category -> category.name())
            .collect(Collectors.toList());
    }

}
