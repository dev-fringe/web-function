package dev.fringe.web.function;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.UndertowHttpHandlerAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import dev.fringe.web.function.configuration.RoutingConfiguration;
import dev.fringe.web.function.configuration.TaskSchedulerConfiguration;
import dev.fringe.web.function.configuration.WebClientConfiguration;
import dev.fringe.web.function.domain.Person;
import io.undertow.Undertow;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFlux
@EnableScheduling
@Import({RoutingConfiguration.class, WebClientConfiguration.class, TaskSchedulerConfiguration.class})
public class Server implements InitializingBean{

	@Autowired ApplicationContext applicationContext;
	@Autowired WebClient webClient;

	public static void main(String[] args) throws Exception {
		new AnnotationConfigApplicationContext(Server.class);
	}

	public void afterPropertiesSet() throws Exception {
		HttpHandler httpHandler = WebHttpHandlerBuilder.applicationContext(applicationContext).build();
		UndertowHttpHandlerAdapter adapter = new UndertowHttpHandlerAdapter(httpHandler);
		int port = Optional.ofNullable(System.getenv("PORT")).map(Integer::parseInt).orElse(8080);
		Undertow server = Undertow.builder().addHttpListener(port, "0.0.0.0").setHandler(adapter).build();
		server.start();
	}

	/**
	 * for heroku
	 */
	@Scheduled(fixedRateString = "60000", initialDelay = 30000) 
	public void scheduleTest() {
		int port = Optional.ofNullable(System.getenv("PORT")).map(Integer::parseInt).orElse(8080);
		if(port != 8080) {
			Flux<Person> person = webClient.get().uri("person").retrieve().bodyToFlux(Person.class);
			Mono<List<Person>> persons = person.collectList();
			System.out.println(persons.block());
		}
	}
}
