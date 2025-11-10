package project.gasnow.jido.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GasStation {

    private String gsId;
    private BigDecimal xCoord;
    private BigDecimal yCoord;
    private String brandCode;
    private String stationName;
    private String address;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters

    public String getGsId() {
        return gsId;
    }

    public void setGsId(String gsId) {
        this.gsId = gsId;
    }

    public BigDecimal getxCoord() {
        return xCoord;
    }

    public void setxCoord(BigDecimal xCoord) {
        this.xCoord = xCoord;
    }

    public BigDecimal getyCoord() {
        return yCoord;
    }

    public void setyCoord(BigDecimal yCoord) {
        this.yCoord = yCoord;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
