package com.sparta.hotdeal.company.domain.entity;

public enum CompanyStatusEnum {
    REQUESTED("REQUESTED"),
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED");

    private final String status;

    CompanyStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
