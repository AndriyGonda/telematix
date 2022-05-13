package org.telematix.dto;

import com.google.gson.annotations.SerializedName;

public class GeopositionDto {
    @SerializedName("lat")
    private float latitude;
    @SerializedName("lon")
    private float longitude;

    @SerializedName("_type")
    private String type;

    public String getType() {
        return type;
    }

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
