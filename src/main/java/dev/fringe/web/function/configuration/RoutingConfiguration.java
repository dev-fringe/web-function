
package dev.fringe.web.function.configuration;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RequestPredicates.method;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import dev.fringe.web.function.handler.PersonHandler;
import dev.fringe.web.function.repository.DummyPersonRepository;
import dev.fringe.web.function.repository.PersonRepository;

@Configuration
public class RoutingConfiguration {

	@Bean
	public PersonRepository repository() {
		return new DummyPersonRepository();
	}

	@Bean
	public PersonHandler handler(PersonRepository repository) {
		return new PersonHandler(repository);
	}

	@Bean
	public RouterFunction<ServerResponse> routerFunction(PersonHandler handler) {
		return nest(path("/person"),nest(accept(APPLICATION_JSON),route(GET("/{id}"), handler::getPerson)
				.andRoute(method(HttpMethod.GET), handler::listPeople))
				.andRoute(POST("/").and(contentType(APPLICATION_JSON)), handler::createPerson));
	}
}
