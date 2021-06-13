package com.microservices.twitter.autoconfigure.config;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Slf4j
@Configuration
@ConditionalOnClass(value = { Twitter.class, TwitterFactory.class })
@EnableConfigurationProperties(value = TwitterConfig.class)
public class TwitterAutoConfiguration {

	@Autowired
	private TwitterConfig twitterConfig;

	@Bean
	@ConditionalOnMissingBean
	public TwitterFactory twitterFactory() {

		String msg = "Twitter4j properties not configured properly."
				+ " Please check twitter4j.* properties settings in configuration file.";
		if (Objects.nonNull(this.twitterConfig.getOauth())) {
			if (Objects.isNull(this.twitterConfig.getOauth().getConsumerKey())
					|| Objects.isNull(this.twitterConfig.getOauth().getConsumerSecret())
					|| Objects.isNull(this.twitterConfig.getOauth().getAccessToken())
					|| Objects.isNull(this.twitterConfig.getOauth().getAccessTokenSecret())) {
				log.error(msg);
				throw new RuntimeException(msg);
			}
		} else {
			throw new RuntimeException(msg);
		}

		log.info("default twitterFactory initialized");
		
		final ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setDebugEnabled(twitterConfig.isDebug());
		configurationBuilder.setOAuthConsumerKey(twitterConfig.getOauth().getConsumerKey());
		configurationBuilder.setOAuthConsumerSecret(twitterConfig.getOauth().getConsumerSecret());
		configurationBuilder.setOAuthAccessToken(twitterConfig.getOauth().getAccessToken());
		configurationBuilder.setOAuthAccessTokenSecret(twitterConfig.getOauth().getAccessTokenSecret());
		final TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());
		return twitterFactory;
	}

	@Bean
	@ConditionalOnMissingBean
	public Twitter twitter(TwitterFactory twitterFactory) {
		log.info("default twitter initialized");
		return twitterFactory.getInstance();
	}

}
