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

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Data
public final class Context {

	private static byte[] loadPayload(String path) {
		try {
			URL resourceUrl = ClassLoader.getSystemResource(path);
			if (resourceUrl == null) {
				throw new FileNotFoundException(path);
			}
			return Files.readAllBytes(Paths.get(resourceUrl.toURI()));
		} catch (URISyntaxException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private final HttpMethod method;

	private final HttpHeaders requestHeaders;

	private final byte[] requestPayload;

	private final HttpHeaders responseHeaders;

	private final byte[] responsePayload;

	private final HttpStatus statusCode;

	private final URI url;

	@Builder
	Context(@NonNull String url,
			@NonNull HttpMethod method,
			@NonNull HttpStatus statusCode,
			@Singular Map<String, List<String>> requestHeaders,
			@Singular Map<String, List<String>> responseHeaders,
			Payload requestPayload,
			Payload responsePayload) {
		this.url = URI.create(url);
		this.method = method;
		this.statusCode = statusCode;
		this.requestHeaders = new HttpHeaders();
		this.requestHeaders.putAll(requestHeaders);
		this.responseHeaders = new HttpHeaders();
		this.responseHeaders.putAll(responseHeaders);
		if (requestPayload != null) {
			switch (requestPayload.getType()) {
			case RAW_STRING:
				this.requestPayload = requestPayload.getValue().getBytes();
				break;
			case CLASSPATH_RESOURCE:
				this.requestPayload = loadPayload(requestPayload.getValue());
				break;
			default:
				this.requestPayload = null;
			}
		} else {
			this.requestPayload = null;
		}
		if (responsePayload != null) {
			switch (responsePayload.getType()) {
			case RAW_STRING:
				this.responsePayload = responsePayload.getValue().getBytes();
				break;
			case CLASSPATH_RESOURCE:
				this.responsePayload = loadPayload(responsePayload.getValue());
				break;
			default:
				this.responsePayload = new byte[0];
			}
		} else {
			this.responsePayload = new byte[0];
		}
	}

}
