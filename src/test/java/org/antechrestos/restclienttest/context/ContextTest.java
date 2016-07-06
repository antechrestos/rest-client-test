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