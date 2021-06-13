package com.microservices.twitter.autoconfigure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "twitter4j")
public class TwitterConfig {

	private boolean debug;
	private OAuth oauth;

	@Data
	public static class OAuth {
		private String consumerKey;
		private String consumerSecret;
		private String accessToken;
		private String accessTokenSecret;
	}

}
