/*
 * Copyright 2013-&amp;#36;today.year the original author or authors.&#10;&#10;Licensed under the Apache License, Version 2.0 (the &quot;
 * License&quot;);&#10;you may not use this file except in compliance with the License.&#10;You may obtain a copy of the License at&#10;&#10;
 * http://www.apache.org/licenses/LICENSE-2.0&#10;&#10;Unless required by applicable law or agreed to in writing, software&#10;distributed under
 * the License is distributed on an &quot;AS IS&quot; BASIS,&#10;WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.&#10;See
 * the License for the specific language governing permissions and&#10;limitations under the License.
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
			assertArrayEquals(this.context.getRequestPayload(), this.body.toByteArray());
		}

	}

	private void checkQuery() {
		compareMultiValuedMap(this.context.getQueryParameters(), this.queriedParameters,
				key -> "Query parameter " + key + " should be set",
				(key, missingValue) -> "Missing expected value " + missingValue + " for query parameter " + key);
	}

	private void checkHeaders() {
		compareMultiValuedMap(this.context.getRequestHeaders(), this.headers,
				key -> "Header " + key + "should be valued",
				(key, missingValue) -> "Missing expected value " + missingValue + " for request header " + key);
	}

	private void compareMultiValuedMap(Map<String, List<String>> required, Map<String, List<String>> queried,
			Function<String, String> missingKeyMessageGenerator,
			BiFunction<String, String, String> badValue) {
		required.entrySet().stream()
				.peek(entry -> assertTrue(missingKeyMessageGenerator.apply(entry.getKey()), queried.containsKey(entry.getKey())))
				.forEach(entry -> {
					List<String> valuesPresent = queried.get(entry.getKey());
					Optional<String> missingValue = entry.getValue().stream()
							.filter(value -> !valuesPresent.contains(value))
							.findFirst();
					if (missingValue.isPresent()) {
						fail(badValue.apply(entry.getKey(), missingValue.get()));
					}
				});
	}
}