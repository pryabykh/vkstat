package com.pryabykh.vkstat;

import com.pryabykh.vkstat.core.vk.ds.AuthData;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.net.URIBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootTest
@ActiveProfiles("test")
class VkstatApplicationTests {
	@Autowired
	private AuthData authData;

	@Test
	void contextLoads() {
		System.out.println(authData);
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(authData.getOauthHost())
				.queryParam("client_id", authData.getClientId())
				.queryParam("client_secret", authData.getClientSecret())
				.queryParam("redirect_uri", authData.getRedirectUri())
				.queryParam("code", "");
		System.out.println(uriComponentsBuilder.toUriString());
	}

	@Test
	void uriBuilder() throws URISyntaxException {
		final HttpGet httpGet = new HttpGet(authData.getOauthHost());
		URI build = new URIBuilder(httpGet.getUri())
				.addParameter("client_id", authData.getClientId())
				.addParameter("client_secret", authData.getClientSecret())
				.addParameter("redirect_uri", authData.getRedirectUri())
				.addParameter("code", "")
				.build();
		System.out.println(build.toString());;
	}

}
