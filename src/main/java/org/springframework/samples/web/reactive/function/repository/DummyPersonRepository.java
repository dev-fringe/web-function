package org.springframework.samples.web.reactive.function.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.samples.web.reactive.function.domain.Person;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
public class DummyPersonRepository implements PersonRepository {

	private final Map<Integer, Person> people = new HashMap<>();

	public DummyPersonRepository() {
		this.people.put(1, new Person("John Doe", 42));
		this.people.put(2, new Person("Jane Doe", 36));
	}

	public Mono<Person> getPerson(int id) {
		return Mono.justOrEmpty(this.people.get(id));
	}

	public Flux<Person> allPeople() {
		return Flux.fromIterable(this.people.values());
	}

	public Mono<Void> savePerson(Mono<Person> personMono) {
		return personMono.doOnNext(person -> {
			int id = people.size() + 1;
			people.put(id, person);
			log.info("Saved %s with id %d%n", person, id);
		}).thenEmpty(Mono.empty());
	}
}
