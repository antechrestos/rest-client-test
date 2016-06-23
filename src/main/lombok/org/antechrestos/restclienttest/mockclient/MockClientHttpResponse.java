/*
 * Copyright 2013-&amp;#36;today.year the original author or authors.&#10;&#10;Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);&#10;you may not use this file except in compliance with the License.&#10;You may obtain a copy of the License at&#10;&#10;     http://www.apache.org/licenses/LICENSE-2.0&#10;&#10;Unless required by applicable law or agreed to in writing, software&#10;distributed under the License is distributed on an &quot;AS IS&quot; BASIS,&#10;WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.&#10;See the License for the specific language governing permissions and&#10;limitations under the License.
 */

package org.antechrestos.restclienttest.mockclient;

import org.antechrestos.restclienttest.context.Context;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

class MockClientHttpResponse implements ClientHttpResponse {

	private Context context;

	MockClientHttpResponse(Context context) {
		this.context = context;
	}

	@Override
	public void close() {
	}

	@Override
	public InputStream getBody() throws IOException {
		return new ByteArrayInputStream(this.context.getResponsePayload());
	}

	@Override
	public HttpHeaders getHeaders() {
		return this.context.getResponseHeaders();
	}

	@Override
	public int getRawStatusCode() throws IOException {
		return this.context.getStatusCode().value();
	}

	@Override
	public HttpStatus getStatusCode() throws IOException {
		return this.context.getStatusCode();
	}

	@Override
	public String getStatusText() throws IOException {
		return this.context.getStatusCode().name();
	}

}
