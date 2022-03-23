package com.yunhalee.walkerholic.order.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Embeddable;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Embeddable
@Getter
@NoArgsConstructor
public class Address {

    private String name;
    private String country;
    private String city;
    private String zipcode;
    private String address;
    private Integer latitude;
    private Integer longitude;

    @Builder
    public Address(String name, @NonNull String country, @NonNull String city, @NonNull String zipcode, @NonNull String address, Integer latitude, Integer longitude) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.zipcode = zipcode;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
