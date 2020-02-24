/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

/**
 * @author Arjen Poutsma
 */
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