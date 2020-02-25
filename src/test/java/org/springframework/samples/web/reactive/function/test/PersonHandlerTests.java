package org.springframework.samples.web.reactive.function.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.samples.web.reactive.function.configuration.RoutingConfiguration;
import org.springframework.samples.web.reactive.function.domain.Person;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RoutingConfiguration.class})
public class PersonHandlerTests {

	@Autowired RouterFunction<?> routerFunction;
	
	private WebTestClient testClient;
	
	@Before
	public void createTestClient() {
		this.testClient = WebTestClient.bindToRouterFunction(routerFunction)
				.configureClient()
				.baseUrl("http://localhost/person")
				.build();
	}

	@Test
	public void getPerson() throws Exception {
		this.testClient.get()
				.uri("/1")
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Person.class).hasSize(1).returnResult();
	}

	@Test
	public void getPersonNotFound() throws Exception {
		this.testClient.get()
				.uri("/42")
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	public void listPeople() throws Exception {
		this.testClient.get()
				.uri("/")
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Person.class).hasSize(2).returnResult();
	}

	@Test
	public void createPerson() throws Exception {
		Person jack = new Person("Jack Doe", 16);

		this.testClient.post()
				.uri("/")
				.contentType(MediaType.APPLICATION_JSON)
				.syncBody(jack)
				.exchange()
				.expectStatus().isOk();

	}
}