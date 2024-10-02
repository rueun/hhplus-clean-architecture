package io.hhplus.cleanarchitecture.common.exception;

public record ErrorResponse(
        String code,
        String message
) {
}
