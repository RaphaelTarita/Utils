package com.tara.util.persistence.http.request;

import com.tara.util.id.UID;
import com.tara.util.persistence.entity.JGPAEntity;
import com.tara.util.persistence.http.general.HTTPHeader;
import com.tara.util.persistence.http.general.HTTPSymbol;
import com.tara.util.persistence.http.general.HTTPVersion;
import com.tara.util.persistence.json.JSONConvert;
import com.tara.util.persistence.node.config.HTTPConfig;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class HTTPConvert {
    private static final DateTimeFormatter stdFormat = DateTimeFormatter
        .ofPattern("EEE, dd MMM yyyy HH:mm:ss")
        .withLocale(Locale.ENGLISH)
        .withZone(ZoneOffset.UTC);

    private HTTPConvert() {
    }

    private static void appendUrlID(StringBuilder res, String id) {
        if (res.charAt(res.length() - 1) != HTTPSymbol.SLASH.getChar()) {
            res.append(HTTPSymbol.SLASH);
        }
        res.append(id);
    }

    private static void appendParamID(Map<String, String> additionalParams, String id, String idName) {
        additionalParams.put(idName, id);
    }

    private static void appendBodyID(List<JGPAEntity<?>> additionalBodys, String id, String idName) {
        Map<String, Object> idMap = new HashMap<>();
        idMap.put(idName, id);
        additionalBodys.add(JGPAEntity.buildVirtual(idMap, idName));
    }

    private static List<JGPAEntity<?>> resolveAdditionalBodys(Map<String, Map<String, Object>> additionalBodys) {
        List<JGPAEntity<?>> res = new ArrayList<>(additionalBodys.size());
        for (Map.Entry<String, Map<String, Object>> body : additionalBodys.entrySet()) {
            res.add(JGPAEntity.buildVirtual(body.getValue(), body.getKey()));
        }

        return res;
    }

    private static String getParams(Map<String, String> additionalParams) {
        StringBuilder res = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> param : additionalParams.entrySet()) {
            if (first) {
                res.append(HTTPSymbol.QUOT);
                first = false;
            } else {
                res.append(HTTPSymbol.ET);
            }
            res.append(param.getKey())
                .append(HTTPSymbol.EQUALS)
                .append(param.getValue());
        }
        return res.toString();
    }

    private static String getHeaders(Map<HTTPHeader, String> headers) {
        StringBuilder res = new StringBuilder();
        for (Map.Entry<HTTPHeader, String> header : headers.entrySet()) {
            res.append(header.getKey())
                .append(HTTPSymbol.COLON)
                .append(HTTPSymbol.SPACE)
                .append(header.getValue())
                .append(HTTPSymbol.LINEBREAK);
        }
        return res.toString();
    }

    private static String formRequest(HTTPVerb verb, UID id, HTTPConfig config, String idName, List<JGPAEntity<?>> bodys) {
        bodys.addAll(resolveAdditionalBodys(config.additionalBodys()));
        Map<String, String> additionalParams = config.additionalParams();
        StringBuilder res = new StringBuilder(verb.toString());
        res.append(HTTPSymbol.SPACE)
            .append(config.url());
        switch (config.idPos()) {
            case REQUEST_URL:
                appendUrlID(res, id.mapUID());
                break;
            case REQUEST_PARAM:
                appendParamID(additionalParams, id.mapUID(), idName);
                break;
            case REQUEST_BODY:
                appendBodyID(bodys, id.mapUID(), idName);
                break;
        }
        boolean hasBody = !bodys.isEmpty();
        String body = "";

        res.append(getParams(additionalParams))
            .append(HTTPSymbol.SPACE)
            .append(config.version())
            .append(HTTPSymbol.LINEBREAK);

        Map<HTTPHeader, String> headers = new EnumMap<>(HTTPHeader.class);
        if (config.version().order() < HTTPVersion.HTTP_2.order()) {
            headers.put(HTTPHeader.CONNECTION, "keep-alive");
            headers.put(HTTPHeader.KEEP_ALIVE, "timeout=" + config.keepAliveTimeout());
        }
        if (hasBody) {
            body = JSONConvert.toJSON(bodys);
            headers.put(HTTPHeader.CONTENT_LENGTH, String.valueOf(body.getBytes(StandardCharsets.UTF_8).length));
            headers.put(HTTPHeader.CONTENT_TYPE, "application/json");
        }
        headers.put(HTTPHeader.HOST, config.host());
        headers.put(HTTPHeader.DATE, stdFormat.format(Instant.now()) + " GMT");
        headers.putAll(config.additionalHeaders());

        res.append(getHeaders(headers));

        if (hasBody) {
            res.append(HTTPSymbol.LINEBREAK)
                .append(body);
        }
        return res.toString();
    }

    private static String formRequest(HTTPVerb verb, UID id, HTTPConfig config, String idName) {
        return formRequest(
            verb,
            id,
            config,
            idName,
            new ArrayList<>()
        );
    }

    public static String getRequest(UID id, HTTPConfig config, String idName) {
        return formRequest(
            HTTPVerb.GET,
            id,
            config,
            idName
        );
    }

    public static <VO> String putRequest(UID id, JGPAEntity<VO> boundGateway, HTTPConfig config) {
        List<JGPAEntity<?>> bodys = new ArrayList<>();
        bodys.add(boundGateway);
        return formRequest(
            HTTPVerb.PUT,
            id,
            config,
            boundGateway.idName(),
            bodys
        );
    }

    public static String deleteRequest(UID id, HTTPConfig config, String idName) {
        return formRequest(
            HTTPVerb.DELETE,
            id,
            config,
            idName
        );
    }
}