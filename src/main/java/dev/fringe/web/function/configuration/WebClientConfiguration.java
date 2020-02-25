package dev.fringe.web.function.configuration;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@Configurable
public class WebClientConfiguration {

	@Bean
	public WebClient webClient() {
//		return  WebClient.create("http://localhost:8080");
		return  WebClient.create("https://web-function.herokuapp.com");
	}
}
