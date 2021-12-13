package com.yunhalee.walkerholic.useractivity.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String name;

    private String country;

    private String city;

    private String zipcode;

    private String address;

    private Integer latitude;

    private Integer longitude;

    protected Address() {
    }

    public Address(String name, String country, String city, String zipcode, String address) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.zipcode = zipcode;
        this.address = address;
    }

    public Address(String name, String country, String city, String zipcode, String address,
        Integer latitude, Integer longitude) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.zipcode = zipcode;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Address(String country, String city, String zipcode, String address) {
        this.country = country;
        this.city = city;
        this.zipcode = zipcode;
        this.address = address;
    }

    public Address(String country, String city, String zipcode, String address, Integer latitude,
        Integer longitude) {
        this.country = country;
        this.city = city;
        this.zipcode = zipcode;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
