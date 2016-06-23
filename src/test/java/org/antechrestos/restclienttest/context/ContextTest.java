/*
 * Copyright 2013-&amp;#36;today.year the original author or authors.&#10;&#10;Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);&#10;you may not use this file except in compliance with the License.&#10;You may obtain a copy of the License at&#10;&#10;     http://www.apache.org/licenses/LICENSE-2.0&#10;&#10;Unless required by applicable law or agreed to in writing, software&#10;distributed under the License is distributed on an &quot;AS IS&quot; BASIS,&#10;WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.&#10;See the License for the specific language governing permissions and&#10;limitations under the License.
 */

package org.antechrestos.restclienttest.context;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Collections;

import static org.junit.Assert.*;

public class ContextTest {

	@Test
	public void test_queried_parameters_are_merged(){
		Context context = Context.builder()
				.url("http://somewhere.org?param1=value1&param1=value2&param2=value1")
				.queryParameter("param1", Collections.singletonList("otherValue1"))
				.queryParameter("param2", Collections.singletonList("otherValue1"))
				.statusCode(HttpStatus.OK)
				.method(HttpMethod.GET)
				.build();
		assertEquals(2, context.getQueryParameters().size());
		assertTrue(context.getQueryParameters().containsKey("param1"));
		assertEquals(3, context.getQueryParameters().get("param1").size());
		assertTrue(context.getQueryParameters().containsKey("param2"));
		assertEquals(2, context.getQueryParameters().get("param2").size());
	}

}