package com.sparta.hotdeal.company.domain.entity.company;

import lombok.Getter;

@Getter
public enum CompanyStatusEnum {
    REQUESTED(Status.REQUESTED),
    PENDING(Status.PENDING),
    APPROVED(Status.APPROVED),
    REJECTED(Status.REJECTED);

    private final String status;

    CompanyStatusEnum(String status) {
        this.status = status;
    }

    public static class Status {

        public static final String REQUESTED = "REQUESTED";
        public static final String PENDING = "PENDING";
        public static final String APPROVED = "APPROVED";
        public static final String REJECTED = "REJECTED";
    }
}
