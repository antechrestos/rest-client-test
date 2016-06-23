/*
 * Copyright 2013-&amp;#36;today.year the original author or authors.&#10;&#10;Licensed under the Apache License, Version 2.0 (the &quot;
 * License&quot;);&#10;you may not use this file except in compliance with the License.&#10;You may obtain a copy of the License at&#10;&#10;
 * http://www.apache.org/licenses/LICENSE-2.0&#10;&#10;Unless required by applicable law or agreed to in writing, software&#10;distributed under
 * the License is distributed on an &quot;AS IS&quot; BASIS,&#10;WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.&#10;See
 * the License for the specific language governing permissions and&#10;limitations under the License.
 */

package org.antechrestos.restclienttest;

import org.antechrestos.restclienttest.utils.URIHelper;
import org.junit.Test;

import java.net.URI;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestUri {

	@Test
	public void testUri() {
		String url = "http://somewhere.org/toto/titi?param1=1%202%202";
		URI uri = URI.create(url);
		String query = uri.getQuery();
		Map<String, List<String>> queryParameters = null;
		if (query != null) {
			queryParameters = URIHelper.getQueryParameters(uri);
		}
		uri = URIHelper.removeQueryParameters(uri);
		System.err.println(uri);
		if(queryParameters != null)
			queryParameters.entrySet().forEach(entry -> System.err.println(" - " + entry.getKey() + " = " + entry.getValue()));
	}



}
