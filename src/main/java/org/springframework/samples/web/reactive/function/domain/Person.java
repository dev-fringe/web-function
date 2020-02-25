package org.springframework.samples.web.reactive.function.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Person {

	private final String name;

	private final int age;

	public Person(@JsonProperty("name") String name, @JsonProperty("age") int age) {
		this.name = name;
		this.age = age;
	}
}
