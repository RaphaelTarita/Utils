package com.tara.util.persistence.http;

import com.tara.util.id.UID;
import com.tara.util.persistence.entity.JGPAEntity;
import com.tara.util.persistence.node.config.HTTPConfig;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        if (res.charAt(res.length() - 1) != HTTPSymbol.SLASH.getch()) {
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

    public static String getRequest(UID id, HTTPConfig config, String idName) {
        List<JGPAEntity<?>> additionalBodys = resolveAdditionalBodys(config.additionalBodys());
        Map<String, String> additionalParams = config.additionalParams();
        StringBuilder res = new StringBuilder(HTTPVerb.GET.toString());
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
                appendBodyID(additionalBodys, id.mapUID(), idName);
                break;
        }
        res.append(getParams(additionalParams))
            .append(HTTPSymbol.SPACE)
            .append(config.version())
            .append(HTTPSymbol.LINEBREAK);
        return res.toString();
    }
}
