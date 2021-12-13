package com.yunhalee.walkerholic.user.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Level {
    Starter("Starter", 0, 0),
    Bronze("Bronze", 1, 299),
    Silver("Silver", 300, 999),
    Gold("Gold", 1000, 2999),
    Master("Master", 3000, 2147483647);


    private final String name;
    private final Integer min;
    private final Integer max;

    Level(String name, Integer min, Integer max) {
        this.name = name;
        this.min = min;
        this.max = max;
    }


}
