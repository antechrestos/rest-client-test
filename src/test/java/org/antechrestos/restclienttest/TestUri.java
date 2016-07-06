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
