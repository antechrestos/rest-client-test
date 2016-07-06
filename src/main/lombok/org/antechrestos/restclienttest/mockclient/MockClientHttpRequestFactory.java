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

import org.antechrestos.restclienttest.context.Context;
import org.antechrestos.restclienttest.utils.URIHelper;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;

import java.io.IOException;
import java.net.URI;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * The request factory that will be used to mock calls. Build one and register context call using the {@link MockClientHttpRequestFactory}
 */
public class MockClientHttpRequestFactory implements ClientHttpRequestFactory {

	private Map<HttpMethod, Map<URI, Context>> contexts;

	public MockClientHttpRequestFactory() {
		this.contexts = new EnumMap<>(HttpMethod.class);
	}

	public void register(Context context) {
		Map<URI, Context> contextForMethod = this.contexts.get(context.getMethod());
		if (contextForMethod == null) {
			contextForMethod = new HashMap<>();
			this.contexts.put(context.getMethod(), contextForMethod);
		}
		contextForMethod.put(context.getUrl(), context);
	}

	@Override
	public ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
		URI urlWithoutQueriedParameters = URIHelper.removeQueryParameters(url);
		Map<String, List<String>> queriedParameters = URIHelper.getQueryParameters(url);
		final String noContextForRequest = "No registered context for  "+method+ " on "+urlWithoutQueriedParameters;

		Map<URI, Context> contextForMethod = this.contexts.get(method);
		assertNotNull(noContextForRequest, contextForMethod);

		Context context = contextForMethod.get(urlWithoutQueriedParameters);
		assertNotNull(noContextForRequest, context);
		return new MockClientHttpRequest(context, queriedParameters);
	}

}
