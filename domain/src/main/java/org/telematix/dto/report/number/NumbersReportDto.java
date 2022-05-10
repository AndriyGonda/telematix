package org.telematix.dto.report.number;

import java.util.List;

public class NumbersReportDto {
    private double total;
    private double maxValue;
    private double minValue;
    private double averageValue;
    private List<NumberResponseDto> messages;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(double averageValue) {
        this.averageValue = averageValue;
    }

    public List<NumberResponseDto> getMessages() {
        return messages;
    }

    public void setMessages(List<NumberResponseDto> messages) {
        this.messages = messages;
    }
}
