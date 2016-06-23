/*
 * Copyright 2013-&amp;#36;today.year the original author or authors.&#10;&#10;Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);&#10;you may not use this file except in compliance with the License.&#10;You may obtain a copy of the License at&#10;&#10;     http://www.apache.org/licenses/LICENSE-2.0&#10;&#10;Unless required by applicable law or agreed to in writing, software&#10;distributed under the License is distributed on an &quot;AS IS&quot; BASIS,&#10;WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.&#10;See the License for the specific language governing permissions and&#10;limitations under the License.
 */

package org.antechrestos.restclienttest.mockclient;

import org.antechrestos.restclienttest.context.Context;
import org.antechrestos.restclienttest.utils.URIHelper;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
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
