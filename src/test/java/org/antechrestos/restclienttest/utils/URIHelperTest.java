/*
 * Copyright 2013-&amp;#36;today.year the original author or authors.&#10;&#10;Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);&#10;you may not use this file except in compliance with the License.&#10;You may obtain a copy of the License at&#10;&#10;     http://www.apache.org/licenses/LICENSE-2.0&#10;&#10;Unless required by applicable law or agreed to in writing, software&#10;distributed under the License is distributed on an &quot;AS IS&quot; BASIS,&#10;WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.&#10;See the License for the specific language governing permissions and&#10;limitations under the License.
 */

package org.antechrestos.restclienttest.utils;

import org.junit.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class URIHelperTest {

	@Test
	public void test_query_parameters_are_removed(){
		assertEquals("http://somewhere.org",
				URIHelper.removeQueryParameters(URI.create("http://somewhere.org?param1=value1&param2=value2")).toString());
		assertEquals("http://somewhere.org",
				URIHelper.removeQueryParameters(URI.create("http://somewhere.org?")).toString());
	}

	@Test
	public void test_query_parameters_are_read(){
		Map<String, List<String>> queryParameters = URIHelper.getQueryParameters(URI.create("http://somewhere.org?param1=value1&param2=value2&param1=otherValue1"));
		assertTrue(queryParameters.containsKey("param1"));
		assertTrue(queryParameters.containsKey("param2"));
		assertEquals(Arrays.asList("value1", "otherValue1"), queryParameters.get("param1"));
		assertEquals(Collections.singletonList("value2"), queryParameters.get("param2"));
	}

}