/*
 * Copyright 2013-&amp;#36;today.year the original author or authors.&#10;&#10;Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);&#10;you may not use this file except in compliance with the License.&#10;You may obtain a copy of the License at&#10;&#10;     http://www.apache.org/licenses/LICENSE-2.0&#10;&#10;Unless required by applicable law or agreed to in writing, software&#10;distributed under the License is distributed on an &quot;AS IS&quot; BASIS,&#10;WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.&#10;See the License for the specific language governing permissions and&#10;limitations under the License.
 */

package org.antechrestos.restclienttest.context;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import org.antechrestos.restclienttest.utils.URIHelper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	private static void mergeMaps(Map<String, List<String>> container, Map<String, List<String>> other) {
		if(other != null){
			other.entrySet().stream()
					.filter(entry -> entry.getValue() != null)
					.forEach(entry -> {
						container.merge(entry.getKey(), entry.getValue(), (alreadyExisting, otherList) -> {
							alreadyExisting.addAll(otherList);
							return alreadyExisting;
						});
					});
		}
	}

	private final HttpMethod method;

	private final Map<String, List<String>> queryParameters;

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
			@Singular Map<String, List<String>> queryParameters,
			@Singular Map<String, List<String>> requestHeaders,
			@Singular Map<String, List<String>> responseHeaders,
			Payload requestPayload,
			Payload responsePayload) {
		URI urlTemp = URI.create(url);
		this.method = method;
		this.statusCode = statusCode;
		this.queryParameters = new HashMap<>();
		mergeMaps(this.queryParameters, URIHelper.getQueryParameters(urlTemp));
		mergeMaps(this.queryParameters, queryParameters);
		this.url = URIHelper.removeQueryParameters(urlTemp);
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
