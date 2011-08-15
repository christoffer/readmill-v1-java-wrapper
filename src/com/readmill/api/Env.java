package com.readmill.api;

import org.apache.http.HttpHost;

/**
 * The environment to operate against.
 */
public enum Env {
    LIVE("api.readmill.com", "readmill.com"),
    STAGING("api.stage-readmill.com", "stage-readmill.com"),
    LOCAL("api.readmill.local", "readmill.local");

    public final HttpHost apiHost, apiSSLHost, webHost, webSSLHost;

    /**
     * @param apiHostName           the api host
     * @param resourceHostName      the resource (=web host)
     */
    Env(String apiHostName, String resourceHostName) {
        apiHost = new HttpHost(apiHostName, -1, "http");
        apiSSLHost = new HttpHost(apiHostName, -1, "http");

        webHost = new HttpHost(resourceHostName, -1, "http");
        webSSLHost = new HttpHost(resourceHostName, -1, "http");
    }

    public HttpHost getApiHost(boolean secure) {
        return secure ? apiSSLHost : apiHost;
    }

    public HttpHost getWebHost(boolean secure) {
        return secure ? webSSLHost : webHost;
    }
}
