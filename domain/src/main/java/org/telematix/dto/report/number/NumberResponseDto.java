package org.telematix.dto.report.number;

import java.sql.Timestamp;

public class NumberResponseDto {
    private Timestamp timestamp;
    private float value;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
