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