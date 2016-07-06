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

package org.antechrestos.restclienttest.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This tool class is used to handle queries parameters in a given {@link URI}
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class URIHelper {

	/**
	 * Removes queries parameters
	 *
	 * @param uri the uri with query parameters
	 * @return the uri without the query part
	 */
	public static URI removeQueryParameters(URI uri) {
		String query = uri.getRawQuery();
		String uriStr = uri.toString();
		if (query != null) {
			uriStr = uriStr.substring(0, uriStr.length() - query.length());
		}
		if (uriStr.endsWith("?")) {
			uriStr = uriStr.substring(0, uriStr.length() - 1);
		}
		return URI.create(uriStr);
	}

	/**
	 * Read the query parameters
	 *
	 * @param uri the uri with query parameters
	 * @return the query parameters as a key-value {@link Map}
	 */
	public static Map<String, List<String>> getQueryParameters(URI uri) {
		String query = uri.getQuery();
		if (query != null) {
			return Arrays.stream(query.split("&"))
					.map(s -> s.split("="))
					.map(split -> {
						if (split.length > 1) {
							return new AbstractMap.SimpleImmutableEntry<>(split[0], split[1]);
						} else {
							return new AbstractMap.SimpleImmutableEntry<String, String>(split[0], null);
						}
					})
					.collect(Collectors.groupingBy(AbstractMap.SimpleImmutableEntry::getKey,
							Collectors.mapping(AbstractMap.SimpleImmutableEntry::getValue, Collectors.toList())));
		} else {
			return Collections.emptyMap();
		}
	}

}
