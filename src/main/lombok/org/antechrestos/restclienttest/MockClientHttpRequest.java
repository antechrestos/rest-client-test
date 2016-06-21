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

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

class MockClientHttpRequest implements ClientHttpRequest {

	@Getter(onMethod = @__(@Override))
	private final ByteArrayOutputStream body;

	@Getter(onMethod = @__(@Override))
	private final HttpHeaders headers;

	private Context context;

	MockClientHttpRequest(Context context) {
		this.context = context;
		this.headers = new HttpHeaders();
		this.body = new ByteArrayOutputStream();
	}

	@Override
	public ClientHttpResponse execute() throws IOException {
		checkHeaders();
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

	private void checkHeaders() {
		this.context.getRequestHeaders().entrySet().forEach(entry -> {
			assertTrue("Header " + entry.getKey() + "should be valued",
					this.headers.containsKey(entry.getKey()));
			List<String> extpectedValues = entry.getValue();
			List<String> valuesPresent = this.headers.get(entry.getKey());
			Optional<String> missingHeader = extpectedValues.stream()
					.filter(value -> !valuesPresent.contains(value))
					.findFirst();
			if (missingHeader.isPresent()) {
				fail("Missing expected value " + missingHeader.get() + " for request header " + entry.getKey());
			}
		});
	}
}
