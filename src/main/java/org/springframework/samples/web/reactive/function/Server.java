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

package org.springframework.samples.web.reactive.function;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.UndertowHttpHandlerAdapter;
import org.springframework.samples.web.reactive.function.configuration.RoutingConfiguration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import io.undertow.Undertow;

@Configuration
@EnableWebFlux
@Import(RoutingConfiguration.class)
public class Server implements InitializingBean{

	@Autowired AnnotationConfigApplicationContext applicationContext;

	public static void main(String[] args) throws Exception {
		new AnnotationConfigApplicationContext(Server.class).getBean(Server.class);
	}

	public void afterPropertiesSet() throws Exception {
		HttpHandler httpHandler = WebHttpHandlerBuilder.applicationContext(applicationContext).build();
		UndertowHttpHandlerAdapter adapter = new UndertowHttpHandlerAdapter(httpHandler);
		Undertow server = Undertow.builder().addHttpListener(8080, "localhost").setHandler(adapter).build();
		server.start();
	}

}
