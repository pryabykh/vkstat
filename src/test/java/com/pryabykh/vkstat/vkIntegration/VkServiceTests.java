package com.pryabykh.vkstat.vkIntegration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pryabykh.vkstat.core.vk.VkService;
import com.pryabykh.vkstat.core.vk.ds.AuthData;
import com.pryabykh.vkstat.core.vk.ds.AuthResult;
import com.pryabykh.vkstat.core.vk.ds.Online;
import com.pryabykh.vkstat.core.vk.ds.VkUser;
import com.pryabykh.vkstat.core.vk.exceptions.RequestToVkApiFailedException;
import com.pryabykh.vkstat.core.vk.exceptions.VkAuthErrorException;
import io.netty.handler.codec.http.HttpMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest
@ActiveProfiles("test")
public class VkServiceTests {
    private VkService vkService;
    private ClientAndServer mockServer;
    private static final ObjectMapper serializer = new ObjectMapper();
    private final String apiVersion = "5.131";
    @Autowired
    private AuthData authData;
    @Value("${vk.mock_server_port}")
    private int vkMockServerPort;

    @BeforeEach
    public void setupMockServer() {
        mockServer = ClientAndServer.startClientAndServer(vkMockServerPort);
    }

    @AfterEach
    public void tearDownServer() {
        mockServer.stop();
    }

//    @Test
    void auth() throws VkAuthErrorException {
        AuthResult authResult = vkService.auth("8bbd517df500a80b00");
        System.out.println(authResult);
    }

//    @Test
    void getUser() throws RequestToVkApiFailedException {
        Optional<VkUser> user = vkService.getUser("sergdevguy", "b40e71524426998e7d8ff1bf7cf39e072e94b4456ea2cf7927f9907103b17165c6e4653dede53e554d97b");
        System.out.println(user);
    }

    @Test
    void authPositiveMock() throws VkAuthErrorException {
        String authCode = "8bbd517df500a80b00";
        mockServer.when(
                request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath("/access_token")
                        .withQueryStringParameter("client_id", authData.getClientId())
                        .withQueryStringParameter("client_secret", authData.getClientSecret())
                        .withQueryStringParameter("redirect_uri", authData.getRedirectUri())
                        .withQueryStringParameter("code", authCode)
        ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(generateAuthResponseBody())
        );
        AuthResult authResult = vkService.auth("8bbd517df500a80b00");
        Assertions.assertNotNull(authResult.getAccessToken());
        Assertions.assertEquals(0, authResult.getExpiresIn());
        Assertions.assertNotEquals(0, authResult.getUserId());
    }

    @Test
    void authNegativeMock() {
        String authCode = "8bbd517df500a80b00";

        authData.setClientId(null);
        mockServer.when(
                request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath("/access_token")
                        .withQueryStringParameter("client_id", authData.getClientId())
                        .withQueryStringParameter("client_secret", authData.getClientSecret())
                        .withQueryStringParameter("redirect_uri", authData.getRedirectUri())
                        .withQueryStringParameter("code", authCode)
        ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(generateAuthResponseBody())
        );
        VkAuthErrorException exception = assertThrows(VkAuthErrorException.class, () -> {
            vkService.auth(null);
        });
    }

    @Test
    public void getExistingUserOnline() throws RequestToVkApiFailedException {
        String accessToken = "b7945af1ce80f014b789e3f7695b9027d2dc9e2def15b7494191d654bcec80ac1b77eed5584e120dd929a";
        String userId = "33590539";
        mockServer.when(
                request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath("/users.get")
                        .withQueryStringParameter("user_id", userId)
                        .withQueryStringParameter("fields", "online")
                        .withQueryStringParameter("access_token", accessToken)
                        .withQueryStringParameter("v", apiVersion)
        ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(generateGetUserOnlineResult())
        );

        VkUser user = vkService.getUser(userId, accessToken).get();
        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getOnline(), Online.YES);
    }

    @Test
    public void getExistingUserOffline() throws RequestToVkApiFailedException {
        String accessToken = "b7945af1ce80f014b789e3f7695b9027d2dc9e2def15b7494191d654bcec80ac1b77eed5584e120dd929a";
        String userId = "33590539";
        mockServer.when(
                request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath("/users.get")
                        .withQueryStringParameter("user_id", userId)
                        .withQueryStringParameter("fields", "online")
                        .withQueryStringParameter("access_token", accessToken)
                        .withQueryStringParameter("v", apiVersion)
        ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(generateGetUserOfflineResult())
        );

        VkUser user = vkService.getUser(userId, accessToken).get();
        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getOnline(), Online.NO);
    }

    @Test
    public void getNonexistentUse() throws RequestToVkApiFailedException {
        String accessToken = "b7945af1ce80f014b789e3f7695b9027d2dc9e2def15b7494191d654bcec80ac1b77eed5584e120dd929a";
        String userId = "33590539";
        mockServer.when(
                request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath("/users.get")
                        .withQueryStringParameter("user_id", userId)
                        .withQueryStringParameter("fields", "online")
                        .withQueryStringParameter("access_token", accessToken)
                        .withQueryStringParameter("v", apiVersion)
        ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(generateGetNonexistentUseResult())
        );

        Optional<VkUser> optionalVkUser = vkService.getUser(userId, accessToken);
        Assertions.assertFalse(optionalVkUser.isPresent());
    }

    @Test
    public void getUserWithError() throws RequestToVkApiFailedException {
        String accessToken = "b7945af1ce80f014b789e3f7695b9027d2dc9e2def15b7494191d654bcec80ac1b77eed5584e120dd929a";
        String userId = "33590539";
        mockServer.when(
                request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath("/users.get")
                        .withQueryStringParameter("user_id", userId)
                        .withQueryStringParameter("fields", "online")
                        .withQueryStringParameter("access_token", accessToken)
                        .withQueryStringParameter("v", apiVersion)
        ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(generateGetUserResponseWithError())
        );
        assertThrows(RequestToVkApiFailedException.class, () -> {
            vkService.getUser(userId, accessToken);
        });
    }

    private String generateAuthResponseBody() {
        return "{\n" +
                "    \"access_token\": \"b7945af1ce80f014b789e3f7695b9027d2dc9e2def15b7494191d654bcec80ac1b77eed5584e120dd929a\",\n" +
                "    \"expires_in\": 0,\n" +
                "    \"user_id\": 683257828\n" +
                "}";
    }

    private String generateGetUserOnlineResult() {
        return "{\n" +
                "    \"response\": [\n" +
                "        {\n" +
                "            \"id\": 33590539,\n" +
                "            \"first_name\": \"Сергей\",\n" +
                "            \"last_name\": \"Харченко\",\n" +
                "            \"can_access_closed\": true,\n" +
                "            \"is_closed\": false,\n" +
                "            \"online\": 1\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    private String generateGetUserOfflineResult() {
        return "{\n" +
                "    \"response\": [\n" +
                "        {\n" +
                "            \"id\": 33590539,\n" +
                "            \"first_name\": \"Сергей\",\n" +
                "            \"last_name\": \"Харченко\",\n" +
                "            \"can_access_closed\": true,\n" +
                "            \"is_closed\": false,\n" +
                "            \"online\": 0\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    private String generateGetNonexistentUseResult() {
        return "{\n" +
                "    \"response\": []\n" +
                "}";
    }

    private String generateGetUserResponseWithError() {
        return "{\n" +
                "    \"error\": {\n" +
                "        \"error_code\": 5,\n" +
                "        \"error_msg\": \"User authorization failed: invalid access_token (4).\",\n" +
                "        \"request_params\": [\n" +
                "            {\n" +
                "                \"key\": \"user_id\",\n" +
                "                \"value\": \"sergdevguy1\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"key\": \"fields\",\n" +
                "                \"value\": \"online\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"key\": \"v\",\n" +
                "                \"value\": \"5.131\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"key\": \"method\",\n" +
                "                \"value\": \"users.get\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"key\": \"oauth\",\n" +
                "                \"value\": \"1\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
    }

    @Autowired
    public void setVkService(VkService vkService) {
        this.vkService = vkService;
    }
}
