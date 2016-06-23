/*
 * Copyright 2013-&amp;#36;today.year the original author or authors.&#10;&#10;Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);&#10;you may not use this file except in compliance with the License.&#10;You may obtain a copy of the License at&#10;&#10;     http://www.apache.org/licenses/LICENSE-2.0&#10;&#10;Unless required by applicable law or agreed to in writing, software&#10;distributed under the License is distributed on an &quot;AS IS&quot; BASIS,&#10;WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.&#10;See the License for the specific language governing permissions and&#10;limitations under the License.
 */

package org.antechrestos.restclienttest.utils;

import java.net.URI;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class URIHelper {

	public static URI removeQueryParameters(URI uri){
		String query = uri.getRawQuery();
		String uriStr = uri.toString();
		if(query != null){
			uriStr  = uriStr.substring(0, uriStr.length() - query.length());
		}
		if(uriStr.endsWith("?")){
			uriStr = uriStr.substring(0, uriStr.length() - 1);
		}
		return URI.create(uriStr);
	}

	public static Map<String, List<String>> getQueryParameters(URI uri) {
		String query = uri.getQuery();
		if(query != null){
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
