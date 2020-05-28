package com.tara.util.persistence.http.request;

import com.tara.util.persistence.http.general.RequireType;

public enum HTTPVerb {
    CONNECT("CONNECT", RequireType.NOT_GIVEN, RequireType.GIVEN, RequireType.NOT_GIVEN, RequireType.NOT_GIVEN, RequireType.NOT_GIVEN),
    DELETE("DELETE", RequireType.MAY, RequireType.MAY, RequireType.NOT_GIVEN, RequireType.GIVEN, RequireType.NOT_GIVEN),
    GET("GET", RequireType.NOT_GIVEN, RequireType.GIVEN, RequireType.GIVEN, RequireType.GIVEN, RequireType.GIVEN),
    HEAD("HEAD", RequireType.NOT_GIVEN, RequireType.NOT_GIVEN, RequireType.GIVEN, RequireType.GIVEN, RequireType.GIVEN),
    OPTIONS("OPTIONS", RequireType.NOT_GIVEN, RequireType.GIVEN, RequireType.GIVEN, RequireType.GIVEN, RequireType.NOT_GIVEN),
    PATCH("PATCH", RequireType.GIVEN, RequireType.GIVEN, RequireType.NOT_GIVEN, RequireType.NOT_GIVEN, RequireType.NOT_GIVEN),
    POST("POST", RequireType.GIVEN, RequireType.GIVEN, RequireType.NOT_GIVEN, RequireType.NOT_GIVEN, RequireType.CONDITIONAL),
    PUT("PUT", RequireType.GIVEN, RequireType.NOT_GIVEN, RequireType.NOT_GIVEN, RequireType.GIVEN, RequireType.NOT_GIVEN),
    TRACE("TRACE", RequireType.NOT_GIVEN, RequireType.NOT_GIVEN, RequireType.NOT_GIVEN, RequireType.GIVEN, RequireType.NOT_GIVEN);

    private final String value;
    private final RequireType requestBody;
    private final RequireType responseBody;
    private final RequireType safe;
    private final RequireType idempotent;
    private final RequireType cacheable;

    HTTPVerb(String value, RequireType requestBody, RequireType responseBody, RequireType safe, RequireType idempotent, RequireType cacheable) {
        this.value = value;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
        this.safe = safe;
        this.idempotent = idempotent;
        this.cacheable = cacheable;
    }

    public RequireType hasRequestBody() {
        return requestBody;
    }

    public RequireType hasResponseBody() {
        return responseBody;
    }

    public RequireType isSafe() {
        return safe;
    }

    public RequireType isIdempotent() {
        return idempotent;
    }

    public RequireType isCacheable() {
        return cacheable;
    }

    @Override
    public String toString() {
        return value;
    }
}
