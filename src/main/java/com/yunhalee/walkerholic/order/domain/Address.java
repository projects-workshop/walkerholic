package com.yunhalee.walkerholic.order.domain;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address1 = (Address) o;
        return Objects.equals(name, address1.name) && Objects
            .equals(country, address1.country) && Objects.equals(city, address1.city)
            && Objects.equals(zipcode, address1.zipcode) && Objects
            .equals(address, address1.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, country, city, zipcode, address);
    }
}
