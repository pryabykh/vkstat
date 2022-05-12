package com.pryabykh.vkstat.vendor.jsonHttpResponseHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.stereotype.Component;

@Component
public class JsonHttpResponseHandlerImpl implements JsonHttpResponseHandler {
    private final ObjectMapper objectMapper;

    public JsonHttpResponseHandlerImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> HttpClientResponseHandler<T> create(Class<T> valueType) {
        return (ClassicHttpResponse response) -> {
            final int status = response.getCode();
            if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
                final HttpEntity entity = response.getEntity();
                if (entity == null) {
                    throw new ClientProtocolException("Response entity is null");
                }
                try {
                    String jsonResponse = EntityUtils.toString(entity);
                    return objectMapper.readValue(jsonResponse, valueType);
                } catch (final ParseException ex) {
                    throw new ClientProtocolException(ex);
                }
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
    }
}
