/*
 * Copyright 2013-&amp;#36;today.year the original author or authors.&#10;&#10;Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);&#10;you may not use this file except in compliance with the License.&#10;You may obtain a copy of the License at&#10;&#10;     http://www.apache.org/licenses/LICENSE-2.0&#10;&#10;Unless required by applicable law or agreed to in writing, software&#10;distributed under the License is distributed on an &quot;AS IS&quot; BASIS,&#10;WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.&#10;See the License for the specific language governing permissions and&#10;limitations under the License.
 */

package org.antechrestos.restclienttest.context;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
public final class Payload {

	public enum Type {
		/**
		 * Payload is to used as raw
		 */
		RAW_STRING,

		/**
		 * Indicates a resource in the classpath
		 */
		CLASSPATH_RESOURCE
	}

	private final Type type;

	private final String value;

	@Builder
	Payload(@NonNull Type type,
			@NonNull String value) {
		this.type = type;
		this.value = value;
	}

}
