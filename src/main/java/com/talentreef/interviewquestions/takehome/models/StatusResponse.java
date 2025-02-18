package com.talentreef.interviewquestions.takehome.models;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class StatusResponse {

    private Instant timestamp;

    private Integer status;

    private String error;

    private String message;

    public static StatusResponse from(Integer statusCode, Exception exception, String message) {
        return StatusResponse.builder()
                .status(statusCode)
                .timestamp(Instant.now())
                .error(exception.getClass().getSimpleName())
                .message(message)
                .build();
    }
}
