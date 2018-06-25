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

package org.antechrestos.restclienttest.mockclient;

import lombok.Getter;
import org.antechrestos.restclienttest.context.Context;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.*;

class MockClientHttpRequest implements ClientHttpRequest {

	@Getter(onMethod = @__(@Override))
	private final ByteArrayOutputStream body;

	@Getter(onMethod = @__(@Override))
	private final HttpHeaders headers;

	private Context context;

	private Map<String, List<String>> queriedParameters;

	MockClientHttpRequest(Context context, Map<String, List<String>> queriedParameters) {
		this.context = context;
		this.headers = new HttpHeaders();
		this.body = new ByteArrayOutputStream();
		this.queriedParameters = queriedParameters;
	}

	@Override
	public ClientHttpResponse execute() throws IOException {
		checkHeaders();
		checkQuery();
		checkBody();
		return new MockClientHttpResponse(this.context);
	}

	@Override
	public HttpMethod getMethod() {
		return this.context.getMethod();
	}

	@Override
	public URI getURI() {
		return this.context.getUrl();
	}

	private void checkBody() throws IOException {
		if (this.context.getRequestPayload() != null) {
            byte[] bytes = this.body.toByteArray();
            try {
                assertArrayEquals(this.context.getRequestPayload(), bytes);
            } catch (AssertionError ae){
                throw new AssertionError(String.format("actual request body [%s] doesn't match expected [%s]",
                        new String(bytes),
                        new String(this.context.getRequestPayload())));
            }
		}

	}

	private interface InvalidValueMessageBuilder {
		String apply(String key, String expected, List<String> got);
	}

	private void checkQuery() {
		compareMultiValuedMap(this.context.getQueryParameters(), this.queriedParameters,
				key -> "Query parameter " + key + " should be set",
				(key, missingValue, queriedValues) -> "Missing expected value \"" + missingValue + "\" for query parameter " + key+". Got: "+ queriedValues);
	}

	private void checkHeaders() {
		compareMultiValuedMap(this.context.getRequestHeaders(), this.headers,
				key -> "Header " + key + "should be valued",
				(key, missingValue, queriedValues) -> "Missing expected value \"" + missingValue + "\" for request header " + key+". Got: "+ queriedValues);
	}

	private void compareMultiValuedMap(Map<String, List<String>> required, Map<String, List<String>> queried,
			Function<String, String> missingKeyMessageGenerator,
									   InvalidValueMessageBuilder badValue) {
		required.entrySet().stream()
				.peek(entry -> assertTrue(missingKeyMessageGenerator.apply(entry.getKey()), queried.containsKey(entry.getKey())))
				.forEach(entry -> {
					List<String> valuesPresent = queried.get(entry.getKey());
					Optional<String> missingValue = entry.getValue().stream()
							.filter(value -> !valuesPresent.contains(value))
							.findFirst();
					if (missingValue.isPresent()) {
						fail(badValue.apply(entry.getKey(), missingValue.get(), valuesPresent));
					}
				});
	}
}
