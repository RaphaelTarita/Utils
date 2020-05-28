package com.tara.util.persistence.http.general;

public enum HTTPHeader {
    // important
    CONNECTION("Connection"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    DATE("Date"),
    HOST("Host"),
    KEEP_ALIVE("Keep-Alive"),

    // standard
    ACCEPT("Accept"),
    ACCEPT_CH("Accept-CH"),
    ACCEPT_CH_LIFETIME("Accept-CH-Lifetime"),
    ACCEPT_CHARSET("Accept-Charset"),
    ACCEPT_ENCODING("Accept-Encoding"),
    ACCEPT_LANGUAGE("Accept-Language"),
    ACCEPT_PATCH("Accept-Patch"),
    ACCEPT_RANGES("Accept-Ranges"),
    ACCESS_CONTROL_ALLOW_CREDENTIALS("Access-Control-Allow-Credentials"),
    ACCESS_CONTROL_ALLOW_HEADERS("Access-Control-Allow-Headers"),
    ACCESS_CONTROL_ALLOW_METHODS("Access-Control-Allow-Methods"),
    ACCESS_CONTROL_ALLOW_ORIGIN("Access-Control-Allow-Origin"),
    ACCESS_CONTROL_EXPOSE_HEADERS("Access-Control-Expose-Headers"),
    ACCESS_CONTROL_MAX_AGE("Access-Control-Max-Age"),
    ACCESS_CONTROL_REQUEST_HEADERS("Access-Control-Request-Headers"),
    ACCESS_CONTROL_REQUEST_METHOD("Access-Control-Request-Method"),
    AGE("Age"),
    ALLOW("Allow"),
    ALT_SVC("Alt-Svc"),
    AUTHORIZATION("Authorization"),
    CACHE_CONTROL("Cache-Control"),
    CLEAR_SITE_DATA("Clear-Site-Data"),
    CONTENT_DISPOSITION("Content-Disposition"),
    CONTENT_ENCODING("Content-Encoding"),
    CONTENT_LANGUAGE("Content-Language"),
    CONTENT_LOCATION("Content-Location"),
    CONTENT_RANGE("Content-Range"),
    CONTENT_SECURITY_POLICY("Content-Security-Policy"),
    CONTENT_SECURITY_POLICY_REPORT_ONLY("Content-Security-Policy-Report-Only"),
    COOKIE("Cookie"),
    COOKIE2("Cookie2"),
    CROSS_ORIGIN_RESOURCE_POLICY("Cross-Origin-Resource-Policy"),
    DNT("DNT"),
    DPR("DPR"),
    DEVICE_MEMORY("Device-Memory"),
    DIGEST("Digest"),
    ETAG("ETag"),
    EARLY_DATA("Early-Data"),
    EXPECT("Expect"),
    EXPECT_CT("Expect-CT"),
    EXPIRES("Expires"),
    FEATURE_POLICY("Feature-Policy"),
    FORWARDED("Forwarded"),
    FROM("From"),
    IF_MATCH("If-Match"),
    IF_MODIFIED_SINCE("If-Modified-Since"),
    IF_NONE_MATCH("If-None-Match"),
    IF_RANGE("If-Range"),
    IF_UNMODIFIED_SINCE("If-Unmodified-Since"),
    INDEX("Index"),
    LARGE_ALLOCATION("Large-Allocation"),
    LAST_MODIFIED("Last-Modified"),
    LINK("Link"),
    LOCATION("Location"),
    NEL("NEL"),
    ORIGIN("Origin"),
    PRAGMA("Pragma"),
    PROXY_AUTHENTICATE("Proxy-Authenticate"),
    PROXY_AUTHORIZATION("Proxy-Authorization"),
    PUBLIC_KEY_PINS("Public-Key-Pins"),
    PUBLIC_KEY_PINS_REPORT_ONLY("Public-Key-Pins-Report-Only"),
    RANGE("Range"),
    REFERER("Referer"),
    REFERRER_POLICY("Referrer-Policy"),
    RETRY_AFTER("Retry-After"),
    SAVE_DATA("Save-Data"),
    SEC_FETCH_DEST("Sec-Fetch-Dest"),
    SEC_FETCH_MODE("Sec-Fetch-Mode"),
    SEC_FETCH_SITE("Sec-Fetch-Site"),
    SEC_FETCH_USER("Sec-Fetch-User"),
    SEC_WEBSOCKET_ACCEPT("Sec-WebSocket-Accept"),
    SERVER("Server"),
    SERVER_TIMING("Server-Timing"),
    SET_COOKIE("Set-Cookie"),
    SET_COOKIE2("Set-Cookie2"),
    SOURCEMAP("SourceMap"),
    STRICT_TRANSPORT_SECURITY("Strict-Transport-Security"),
    TE("TE"),
    TIMING_ALLOW_ORIGIN("Timing-Allow-Origin"),
    TK("Tk"),
    TRAILER("Trailer"),
    TRANSFER_ENCODING("Transfer-Encoding"),
    UPGRADE_INSECURE_REQUESTS("Upgrade-Insecure-Requests"),
    USER_AGENT("User-Agent"),
    VARY("Vary"),
    VIA("Via"),
    WWW_AUTHENTICATE("WWW-Authenticate"),
    WANT_DIGEST("Want-Digest"),
    WARNING("Warning"),

    // common non-standard
    X_CONTENT_TYPE_OPTIONS("X-Content-Type-Options"),
    X_DNS_PREFETCH_CONTROL("X-DNS-Prefetch-Control"),
    X_FORWARDED_FOR("X-Forwarded-For"),
    X_FORWARDED_HOST("X-Forwarded-Host"),
    X_FORWARDED_PROTO("X-Forwarded-Proto"),
    X_FRAME_OPTIONS("X-Frame-Options"),
    X_XSS_PROTECTION("X-XSS-Protection");


    private final String value;

    HTTPHeader(String value) {
        this.value = value;
    }

    public static HTTPHeader of(String value) {
        for (HTTPHeader header : values()) {
            if (header.value.equalsIgnoreCase(value)) {
                return header;
            }
        }

        throw new IllegalArgumentException("Invalid HTTP Header: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}