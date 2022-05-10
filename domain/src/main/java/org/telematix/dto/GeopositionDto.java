package org.telematix.dto;

public class GeopositionDto {
    private float latitude;
    private float longitude;

    public float getLatitude() {
        return latitude;
    }

    public GeopositionDto() {
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
