/*
 * Copyright 2013-2016 the original author or authors.
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

package org.antechrestos.restclienttest;

import org.antechrestos.restclienttest.context.Context;
import org.antechrestos.restclienttest.context.Payload;
import org.antechrestos.restclienttest.mockclient.MockClientHttpRequestFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MockClientHttpRequestFactoryTest {

	private MockClientHttpRequestFactory clientHttpRequestFactory;

	private RestTemplate restTemplate = new RestTemplate();

	@Test(expected = AssertionError.class)
	public void bad_request_body_fails() {
		this.clientHttpRequestFactory.register(
				Context.builder()
						.url("http://somewhere.org")
						.statusCode(HttpStatus.OK)
						.method(HttpMethod.GET)
						.requestPayload(Payload.builder()
								.type(Payload.Type.RAW_STRING)
								.value("some body")
								.build())
						.build()
		);
		this.restTemplate.exchange("http://somewhere.org", HttpMethod.GET, new HttpEntity<>("some other  body"), String.class);
	}

	@Test(expected = AssertionError.class)
	public void bad_url_fails() {
		this.clientHttpRequestFactory.register(
				Context.builder()
						.url("http://somewhere.org")
						.method(HttpMethod.GET)
						.statusCode(HttpStatus.OK)
						.build()
		);
		this.restTemplate.exchange("http://somewhere-else.org", HttpMethod.GET, HttpEntity.EMPTY, String.class);

	}

	@Test
	public void good_request_body_succeeds() {
		this.clientHttpRequestFactory.register(
				Context.builder()
						.url("http://somewhere.org")
						.statusCode(HttpStatus.OK)
						.method(HttpMethod.GET)
						.requestPayload(Payload.builder()
								.type(Payload.Type.RAW_STRING)
								.value("some body")
								.build())
						.build()
		);
		this.restTemplate.exchange("http://somewhere.org", HttpMethod.GET, new HttpEntity<>("some body"), String.class);
	}

	@Test
	public void no_check_on_request_body_should_succeed() {
		this.clientHttpRequestFactory.register(
				Context.builder()
						.url("http://somewhere.org")
						.statusCode(HttpStatus.OK)
						.method(HttpMethod.GET)
						.build()
		);
		this.restTemplate.exchange("http://somewhere.org", HttpMethod.GET, new HttpEntity<>("some body"), String.class);
	}

	@Test
	public void good_response_status_code_and_body_is_returned() {
		this.clientHttpRequestFactory.register(
				Context.builder()
						.url("http://somewhere.org")
						.statusCode(HttpStatus.OK)
						.method(HttpMethod.GET)
						.responsePayload(Payload.builder()
								.type(Payload.Type.RAW_STRING)
								.value("some body")
								.build())
						.build()
		);
		ResponseEntity<String> result = this.restTemplate
				.exchange("http://somewhere.org", HttpMethod.GET, HttpEntity.EMPTY, String.class);
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals("some body", result.getBody());
	}

	@Test
	public void good_url_succeeds() {
		this.clientHttpRequestFactory.register(
				Context.builder()
						.url("http://somewhere.org")
						.method(HttpMethod.GET)
						.statusCode(HttpStatus.OK)
						.build()
		);
		this.restTemplate.exchange("http://somewhere.org", HttpMethod.GET, HttpEntity.EMPTY, String.class);
	}

	@Test(expected = AssertionError.class)
	public void request_header_bad_valued_fails() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("some-header", "some-value");
		this.clientHttpRequestFactory.register(
				Context.builder()
						.url("http://somewhere.org")
						.statusCode(HttpStatus.OK)
						.method(HttpMethod.GET)
						.requestHeader("some-header", singletonList("some-other-value"))
						.build()
		);
		this.restTemplate.exchange("http://somewhere.org", HttpMethod.GET, new HttpEntity<String>(headers), String.class);

	}

	@Test(expected = AssertionError.class)
	public void request_header_not_present_fails() {
		this.clientHttpRequestFactory.register(
				Context.builder()
						.url("http://somewhere.org")
						.statusCode(HttpStatus.OK)
						.method(HttpMethod.GET)
						.requestHeader("some-header", singletonList("some-value"))
						.build()
		);
		this.restTemplate.exchange("http://somewhere.org", HttpMethod.GET, HttpEntity.EMPTY, String.class);

	}

	@Test
	public void request_header_present_succeeds() {
		this.clientHttpRequestFactory.register(
				Context.builder()
						.url("http://somewhere.org")
						.statusCode(HttpStatus.OK)
						.method(HttpMethod.GET)
						.requestHeader("some-header", singletonList("some-value"))
						.build()
		);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("some-header", "some-value");
		this.restTemplate.exchange("http://somewhere.org", HttpMethod.GET, new HttpEntity<String>(httpHeaders), String.class);

	}

	@Before
	public void setUp() {
		this.clientHttpRequestFactory = new MockClientHttpRequestFactory();
		this.restTemplate.setRequestFactory(this.clientHttpRequestFactory);
	}

	@Test
	public void status_code_set_succeeds() {
		this.clientHttpRequestFactory.register(
				Context.builder()
						.url("http://somewhere.org")
						.statusCode(HttpStatus.OK)
						.method(HttpMethod.GET)
						.build()
		);
		this.restTemplate.exchange("http://somewhere.org", HttpMethod.GET, HttpEntity.EMPTY, String.class);

	}

	@Test(expected = AssertionError.class)
	public void query_set_by_url_are_checked(){
		this.clientHttpRequestFactory.register(
				Context.builder()
						.url("http://somewhere.org?param1=value1&param2=value2")
						.statusCode(HttpStatus.OK)
						.method(HttpMethod.GET)
						.build()
		);
		//here we submit a missing value
		this.restTemplate.exchange("http://somewhere.org?param1=value1", HttpMethod.GET, HttpEntity.EMPTY, String.class);
	}

	@Test(expected = AssertionError.class)
	public void query_set_by_map_are_checked_and_fails(){
		this.clientHttpRequestFactory.register(
				Context.builder()
						.url("http://somewhere.org")
						.queryParameter("param1", singletonList("value1"))
						.queryParameter("param2", singletonList("value2"))
						.statusCode(HttpStatus.OK)
						.method(HttpMethod.GET)
						.build()
		);
		//here we submit a bad value
		this.restTemplate.exchange("http://somewhere.org?param1=value1&param2=value3", HttpMethod.GET, HttpEntity.EMPTY, String.class);
	}

	@Test
	public void query_set_by_map_are_checked_and_succeed(){
		this.clientHttpRequestFactory.register(
				Context.builder()
						.url("http://somewhere.org/ids/toto")
						.queryParameter("param1", singletonList("2018-06-25T04:30:00.000Z"))
						.queryParameter("param2", singletonList("value2"))
						.statusCode(HttpStatus.OK)
						.method(HttpMethod.GET)
						.build()
		);
		this.restTemplate.exchange("http://somewhere.org/ids/{id}?param1={value_param_1}&param2={value_param_2}",
                HttpMethod.GET, HttpEntity.EMPTY, String.class, "toto", "2018-06-25T04:30:00.000Z", "value2");
	}

	@Test
	public void check_body_fails_with_message(){
		this.clientHttpRequestFactory.register(
				Context.builder()
						.url("http://somewhere.org")
						.method(HttpMethod.POST)
						.requestPayload(Payload.builder()
								.type(Payload.Type.RAW_STRING)
								.value("the expected body")
								.build())
						.statusCode(HttpStatus.OK)
						.build());
		//here we submit a bad payload
        try{
            this.restTemplate.exchange("http://somewhere.org?param1=value1&param2=value3", HttpMethod.POST, new HttpEntity<>("the bad body"), String.class);
            fail("Should have failed");
        }catch (AssertionError ase){
            assertEquals(ase.getMessage(), "actual request body [the bad body] doesn't match expected [the expected body]");
        }

	}


}