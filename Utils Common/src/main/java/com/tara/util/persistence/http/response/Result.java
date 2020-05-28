package com.tara.util.persistence.http.response;

import com.tara.util.persistence.http.general.HTTPHeader;
import com.tara.util.persistence.http.general.HTTPVersion;
import com.tara.util.persistence.json.FormatException;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Result {
    private static final Pattern FIRST_LINE = Pattern.compile("(HTTP/[0-9]+(?:.[0-9]+)?)\\s([0-9]{3})[a-zA-Z\\s\\-']*\\n");
    private static final Pattern HEADER = Pattern.compile("([a-zA-Z\\-]+):\\s(.*)\\n");
    private static final Pattern BODY = Pattern.compile("(.*)");
    private final Future<String> futureResponse;
    private final String response;
    private HTTPVersion version;
    private HTTPResponseCode responseCode;
    private final Map<HTTPHeader, String> headers;
    private String body;
    private boolean processed;
    private Exception processException;
    private boolean used;

    private Result(String response, Future<String> futureResponse) {
        this.futureResponse = futureResponse;
        this.response = response;
        headers = new EnumMap<>(HTTPHeader.class);
        processed = false;
        processException = null;
        used = false;
    }

    public Result(String response) {
        this(response, null);
    }

    public Result(Future<String> response) {
        this(null, response);
    }

    private void process() throws FormatException, ExecutionException, InterruptedException {
        String resp;
        if (futureResponse != null) {
            resp = futureResponse.get();
        } else {
            resp = response;
        }

        Matcher first = FIRST_LINE.matcher(resp);
        if (!first.find()) {
            throw new FormatException("Invalid HTTP Response");
        }
        version = HTTPVersion.of(first.group(1));
        responseCode = HTTPResponseCode.of(Integer.parseInt(first.group(2)));

        resp = resp.substring(first.end());

        Matcher header = HEADER.matcher(resp);
        int end = -1;
        while (header.find()) {
            end = header.end();
            headers.put(
                HTTPHeader.of(header.group(1)),
                header.group(2)
            );
        }

        resp = resp.substring(end);
        resp = resp.replace("\n", "");

        Matcher body = BODY.matcher(resp);
        if (body.matches()) {
            this.body = body.group();
        }

        processed = true;
    }

    private void processOrCached() {
        if (!processed) {
            try {
                process();
            } catch (Exception ex) {
                processException = ex;
            }
        }
    }

    public boolean successful() {
        processOrCached();
        return processException == null && responseCode.classify().successful();
    }

    public HTTPVersion getVersion() {
        processOrCached();
        return version;
    }

    public HTTPResponseCode getResponseCode() {
        processOrCached();
        return responseCode;
    }

    public Map<HTTPHeader, String> getHeaders() {
        processOrCached();
        return headers;
    }

    public boolean hasBody() {
        processOrCached();
        return body != null;
    }

    public String getBody() {
        processOrCached();
        return body;
    }

    public boolean hasException() {
        processOrCached();
        return processException != null;
    }

    public Exception getException() {
        processOrCached();
        return processException;
    }

    public void setUsed() {
        used = true;
    }

    public boolean wasUsed() {
        return used;
    }
}