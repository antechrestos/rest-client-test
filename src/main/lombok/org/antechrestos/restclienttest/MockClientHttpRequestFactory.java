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

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class MockClientHttpRequestFactory implements ClientHttpRequestFactory {

	private Map<HttpMethod, Map<URI, Context>> contexts;

	public MockClientHttpRequestFactory() {
		this.contexts = new HashMap<>();
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
		Map<URI, Context> contextForMethod = this.contexts.get(method);
		assertNotNull("No context registered for method " + method.name(), contextForMethod);
		Context context = contextForMethod.get(url);
		assertNotNull("No " + method.name() + " context registered for url " + url, context);
		return new MockClientHttpRequest(context);
	}

}
