package com.pryabykh.vkstat.vendor.jsonHttpResponseHandler;

import org.apache.hc.core5.http.io.HttpClientResponseHandler;

public interface JsonHttpResponseHandler {
    <T> HttpClientResponseHandler<T> create(Class<T> valueType);
}
