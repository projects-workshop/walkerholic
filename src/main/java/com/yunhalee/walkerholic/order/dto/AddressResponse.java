package com.yunhalee.walkerholic.order.dto;

import com.yunhalee.walkerholic.order.domain.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddressResponse {

    private String name;
    private String country;
    private String city;
    private String zipcode;
    private String address;
    private Integer latitude;
    private Integer longitude;

    public AddressResponse(String name, String country, String city, String zipcode, String address) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.zipcode = zipcode;
        this.address = address;
    }

    public AddressResponse(String name, String country, String city, String zipcode, String address, Integer latitude, Integer longitude) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.zipcode = zipcode;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public AddressResponse(Address address) {
        this.name = address.getName();
        this.country = address.getCountry();
        this.city = address.getCity();
        this.zipcode = address.getZipcode();
        this.address = address.getAddress();
        this.latitude = address.getLatitude();
        this.longitude = address.getLongitude();
    }
}
