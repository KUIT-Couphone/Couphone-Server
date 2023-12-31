package com.example.couphoneserver.common.datatype;

import lombok.Builder;
import lombok.Data;

@Data
public class Coordinate {
    private double longitude;
    private double latitude;

    @Builder
    public Coordinate(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
