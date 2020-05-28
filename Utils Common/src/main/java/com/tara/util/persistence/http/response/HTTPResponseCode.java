package com.tara.util.persistence.http.response;

public enum HTTPResponseCode {
    // 100
    CONTINUE_100(100, "Continue"),
    SWITCHING_PROTOCOLS_101(101, "Switching Protocols"),
    EARLY_HINTS_103(103, "Early Hints"),

    // 200
    OK_200(200, "OK"),
    CREATED_201(201, "Created"),
    ACCEPTED_202(202, "Accepted"),
    NON_AUTHORITATIVE_INFORMATION_203(203, "Non-Authoritative Information"),
    NO_CONTENT_204(204, "No Content"),
    RESET_CONTENT_205(205, "Reset Content"),
    PARTIAL_CONTENT_206(206, "Partial Content"),

    // 300
    MULTIPLE_CHOICES_300(300, "Multiple Choices"),
    MOVED_PERMANENTLY_301(301, "Moved Permanently"),
    FOUND_302(302, "Found"),
    SEE_OTHER_303(303, "See Other"),
    NOT_MODIFIED_304(304, "Not Modified"),
    TEMPORARY_REDIRECT_307(307, "Temporary Redirect"),
    PERMANENT_REDIRECT_308(308, "Permanent Redirect"),

    // 400
    BAD_REQUEST_400(400, "Bad Request"),
    UNAUTHORIZED_401(401, "Unauthorized"),
    PAYMENT_REQUIRED_402(402, "Payment Required"),
    FORBIDDEN_403(403, "Forbidden"),
    NOT_FOUND_404(404, "Not Found"),
    METHOD_NOT_ALLOWED_405(405, "Method Not Allowed"),
    NOT_ACCEPTABLE_406(406, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED_407(407, "Proxy Authentication Required"),
    REQUEST_TIMEOUT_408(408, "Request Timeout"),
    CONFLICT_409(409, "Conflict"),
    GONE_410(410, "Gone"),
    LENGTH_REQUIRED_411(411, "Length Required"),
    PRECONDITION_FAILED_412(412, "Precondition Failed"),
    PAYLOAD_TOO_LARGE_413(413, "Payload Too Large"),
    URI_TOO_LONG_414(414, "URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE_415(415, "Unsupported Media Type"),
    RANGE_NOT_SATISFIABLE_416(416, "Range Not Satisfiable"),
    EXPECTATION_FAILED_417(417, "Expectation Failed"),
    IM_A_TEAPOT_418(418, "I'm a teapot"),
    UNPROCESSABLE_ENTITY_422(422, "Unprocessable Entity"),
    TOO_EARLY_425(425, "Too Early"),
    UPGRADE_REQUIRED_426(426, "Upgrade Required"),
    PRECONDITION_REQUIRED_428(428, "Precondition Required"),
    TOO_MANY_REQUESTS_429(429, "Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE_431(431, "Request Header Fields Too Large"),
    UNAVAILABLE_FOR_LEGAL_REASONS_451(451, "Unavailable For Legal Reasons"),

    // 500
    INTERNAL_SERVER_ERROR_500(500, "Internal Server Error"),
    NOT_IMPLEMENTED_501(501, "Not Implemented"),
    BAD_GATEWAY_502(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE_503(503, "Service Unavailable"),
    GATEWAY_TIMEOUT_504(504, "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED_505(505, "HTTP Version Not Supported"),
    VARIANT_ALSO_NEGOTIATES_506(506, "Variant Also Negotiates"),
    INSUFFICIENT_STORAGE_507(507, "Insufficient Storage"),
    LOOP_DETECTED_508(508, "Loop Detected"),
    NOT_EXTENDED_510(510, "Not Extended"),
    NETWORK_AUTHENTICATION_REQUIRED_511(511, "Network Authentication Required");

    private final int code;
    private final String desc;

    HTTPResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static HTTPResponseCode of(int code) {
        for (HTTPResponseCode responseCode : values()) {
            if (responseCode.code == code) {
                return responseCode;
            }
        }

        throw new IllegalArgumentException("Invalid HTTP response code: " + code);
    }

    public ResponseType classify() {
        return ResponseType.of((code / 100) * 100);
    }

    @Override
    public String toString() {
        return code + " " + desc;
    }
}