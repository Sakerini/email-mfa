package com.home.email.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ApiErrorResponse(@Schema(name = "message", example = "message") String message) {}
