package com.tara.util.persistence.node.config;

import com.tara.util.mirror.Mirrorable;
import com.tara.util.mirror.Mirrors;
import com.tara.util.persistence.http.HTTPVersion;
import com.tara.util.persistence.http.IDpos;

import java.util.HashMap;
import java.util.Map;

public class HTTPConfig implements NodeConfig {
    private final String ip;
    private final String url;
    private final int port;
    private final String host;
    private final int keepAliveTimeout;
    private final HTTPVersion version;
    private final IDpos pos;
    private final Map<String, String> additionalParams;
    private final Map<String, String> additionalHeaders;
    private final Map<String, Map<String, Object>> additionalBodys;


    public HTTPConfig(String ip, String url, int port, String host, int keepAliveTimeout, HTTPVersion version, IDpos pos, Map<String, String> additionalParams, Map<String, String> additionalHeaders, Map<String, Map<String, Object>> additionalBodys) {
        this.ip = ip;
        this.url = url;
        this.port = port;
        this.host = host;
        this.keepAliveTimeout = keepAliveTimeout;
        this.version = version;
        this.pos = pos;
        this.additionalParams = additionalParams;
        this.additionalHeaders = additionalHeaders;
        this.additionalBodys = additionalBodys;
    }

    public static class Builder implements Mirrorable<Builder> {
        private String ip;
        private String url;
        private int port;
        private String host;
        private int keepAliveTimeout;
        private HTTPVersion version;
        private IDpos pos;
        private Map<String, String> additionalParams;
        private Map<String, String> additionalHeaders;
        private Map<String, Map<String, Object>> additionalBodys;

        private Builder() {
            ip = "127.0.0.1";
            url = "/";
            port = 80;
            host = null;
            keepAliveTimeout = 10;
            version = HTTPVersion.HTTP_1_1;
            pos = IDpos.REQUEST_PARAM;
            additionalParams = new HashMap<>(0);
            additionalHeaders = new HashMap<>(0);
            additionalBodys = new HashMap<>(0);
        }

        private Builder(HTTPConfig config) {
            ip = config.ip;
            url = config.url;
            port = config.port;
            host = config.host;
            keepAliveTimeout = config.keepAliveTimeout;
            version = config.version;
            pos = config.pos;
            additionalParams = config.additionalParams;
            additionalBodys = new HashMap<>(config.additionalBodys);
        }

        public Builder withIP(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder withURL(String url) {
            this.url = url;
            return this;
        }

        public Builder withPort(int port) {
            this.port = port;
            return this;
        }

        public Builder withHost(String host) {
            this.host = host;
            return this;
        }

        public Builder withKeepAliveTimeout(int keepAliveTimeout) {
            this.keepAliveTimeout = keepAliveTimeout;
            return this;
        }

        public Builder withHTTPVersion(HTTPVersion version) {
            this.version = version;
            return this;
        }

        public Builder withIDpos(IDpos pos) {
            this.pos = pos;
            return this;
        }

        public Builder withAdditionalParams(Map<String, String> additionalParams) {
            this.additionalParams = additionalParams;
            return this;
        }

        public Builder withAdditionalHeaders(Map<String, String> additionalHeaders) {
            this.additionalHeaders = additionalHeaders;
            return this;
        }

        public Builder withAdditionalBodys(Map<String, Map<String, Object>> additionalBody) {
            this.additionalBodys = additionalBody;
            return this;
        }

        public Builder withAdditionalBody(String name, Map<String, Object> additionalBody) {
            this.additionalBodys.put(name, additionalBody);
            return this;
        }

        public HTTPConfig build() {
            String resolvedHost = host == null
                ? ip
                : host;
            Map<String, Map<String, Object>> resolvedAdditionalBodys = new HashMap<>(additionalBodys);
            return new HTTPConfig(
                ip,
                url,
                port,
                resolvedHost,
                keepAliveTimeout,
                version,
                pos,
                additionalParams,
                additionalHeaders,
                resolvedAdditionalBodys
            );
        }

        @Override
        public Builder mirror() {
            return (new Builder())
                .withIP(ip)
                .withURL(url)
                .withPort(port)
                .withHost(host)
                .withKeepAliveTimeout(keepAliveTimeout)
                .withHTTPVersion(version)
                .withIDpos(pos)
                .withAdditionalParams(Mirrors.mirror(additionalParams))
                .withAdditionalHeaders(Mirrors.mirror(additionalHeaders))
                .withAdditionalBodys(Mirrors.mirror(additionalBodys));
        }
    }

    public String ip() {
        return ip;
    }

    public String url() {
        return url;
    }

    public int port() {
        return port;
    }

    public String host() {
        return host;
    }

    public int keepAliveTimeOut() {
        return keepAliveTimeout;
    }

    public HTTPVersion version() {
        return version;
    }

    public IDpos idPos() {
        return pos;
    }

    public Map<String, String> additionalParams() {
        return additionalParams;
    }

    public Map<String, String> additionalHeaders() {
        return additionalHeaders;
    }

    public Map<String, Map<String, Object>> additionalBodys() {
        return additionalBodys;
    }

    public Builder thisBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static HTTPConfig defaultConf() {
        return builder().build();
    }

    @Override
    public HTTPConfig mirror() {
        return thisBuilder().mirror().build();
    }
}
