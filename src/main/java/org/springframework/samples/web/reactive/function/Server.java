package org.springframework.samples.web.reactive.function;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.UndertowHttpHandlerAdapter;
import org.springframework.samples.web.reactive.function.configuration.RoutingConfiguration;
import org.springframework.samples.web.reactive.function.configuration.WebClientConfiguration;
import org.springframework.samples.web.reactive.function.domain.Person;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import io.undertow.Undertow;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFlux
@EnableScheduling
@Import({RoutingConfiguration.class, WebClientConfiguration.class})
public class Server implements InitializingBean{

	@Autowired ApplicationContext applicationContext;
	@Autowired private WebClient webClient;

	public static void main(String[] args) throws Exception {
		String webPort = System.getenv("PORT");
		if(StringUtils.hasLength(webPort) == false) {
			System.setProperty("port", "8080");
			System.setProperty("spring.profiles.active", "LOC");
		}else {
			System.setProperty("port", System.getenv("PORT"));
			System.setProperty("spring.profiles.active", "DEV");
		}
		new AnnotationConfigApplicationContext(Server.class).getBean(Server.class);
	}

	public void afterPropertiesSet() throws Exception {
		HttpHandler httpHandler = WebHttpHandlerBuilder.applicationContext(applicationContext).build();
		UndertowHttpHandlerAdapter adapter = new UndertowHttpHandlerAdapter(httpHandler);
		Undertow server = Undertow.builder().addHttpListener(Integer.valueOf(System.getProperty("port")), "0.0.0.0").setHandler(adapter).build();
		server.start();
	}

	@Scheduled(fixedRateString = "60000", initialDelay = 30000) 
	private void scheduleTest() {
	    String webPort = System.getenv("PORT");
	    if(webPort == null || webPort.isEmpty()) {
	        webPort = "8080";
	    }else { 
			Flux<Person> person = webClient.get().uri("person").retrieve().bodyToFlux(Person.class);
			Mono<List<Person>> persons = person.collectList();
	    }
	}
}
