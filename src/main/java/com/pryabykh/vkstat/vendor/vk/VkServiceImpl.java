package com.pryabykh.vkstat.vendor.vk;

import com.pryabykh.vkstat.core.vk.VkService;
import com.pryabykh.vkstat.core.vk.ds.AuthData;
import com.pryabykh.vkstat.core.vk.ds.AuthResult;
import com.pryabykh.vkstat.core.vk.ds.GetUserResult;
import com.pryabykh.vkstat.core.vk.ds.VkUser;
import com.pryabykh.vkstat.core.vk.exceptions.RequestToVkApiFailedException;
import com.pryabykh.vkstat.core.vk.exceptions.UserNotFoundException;
import com.pryabykh.vkstat.core.vk.exceptions.VkAuthErrorException;
import com.pryabykh.vkstat.vendor.jsonHttpResponseHandler.JsonHttpResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class VkServiceImpl implements VkService {
    private final JsonHttpResponseHandler jsonHttpResponseHandler;
    private final AuthData authData;
    @Value("${vk.methods_host}")
    private String apiMethodsHost;
    private final String apiVersion = "5.131";

    public VkServiceImpl(JsonHttpResponseHandler jsonHttpResponseHandler, AuthData authData) {
        this.jsonHttpResponseHandler = jsonHttpResponseHandler;
        this.authData = authData;
    }

    @Override
    public AuthResult auth(String codeToGetToken) throws VkAuthErrorException {
        try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpGet = new HttpGet(authData.getOauthHost() + "/access_token");
            URI uri = new URIBuilder(httpGet.getUri())
                    .addParameter("client_id", authData.getClientId())
                    .addParameter("client_secret", authData.getClientSecret())
                    .addParameter("redirect_uri", authData.getRedirectUri())
                    .addParameter("code", codeToGetToken)
                    .build();
            httpGet.setUri(uri);
            final HttpClientResponseHandler<AuthResult> responseHandler =
                    jsonHttpResponseHandler.create(AuthResult.class);
            log.info("Executing request " + httpGet.getMethod() + " " + httpGet.getUri());
            return httpClient.execute(httpGet, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
            throw new VkAuthErrorException();
        }
    }

    @Override
    public Optional<VkUser> getUser(String vkUserId, String accessToken) throws RequestToVkApiFailedException {
        try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpGet = new HttpGet(apiMethodsHost + "/users.get");
            URI uri = new URIBuilder(httpGet.getUri())
                    .addParameter("user_id", vkUserId)
                    .addParameter("fields", "online")
                    .addParameter("access_token", accessToken)
                    .addParameter("v", apiVersion)
                    .build();
            httpGet.setUri(uri);
            final HttpClientResponseHandler<GetUserResult> responseHandler =
                    jsonHttpResponseHandler.create(GetUserResult.class);
            log.info("Executing request " + httpGet.getMethod() + " " + httpGet.getUri());
            GetUserResult getUserResult = httpClient.execute(httpGet, responseHandler);
            if (getUserResult.getError() != null) {
                throw new Exception(getUserResult.getError().getErrorMessage());
            }
            List<VkUser> response = getUserResult.getResponse();
            return Optional.ofNullable(response.isEmpty() ? null : response.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RequestToVkApiFailedException();
        }
    }
}
