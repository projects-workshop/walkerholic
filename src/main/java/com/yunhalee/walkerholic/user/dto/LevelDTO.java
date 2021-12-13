package com.yunhalee.walkerholic.user.dto;

import com.yunhalee.walkerholic.user.domain.Level;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LevelDTO {

    private String name;

    private Integer min;

    private Integer max;

    public LevelDTO(Level level) {
        this.name = level.getName();
        this.min = level.getMin();
        this.max = level.getMax();
    }
}
