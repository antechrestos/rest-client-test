# Rest Client Test

## Description

This project brings you a simple library to mock spring rest template in order to test your client.

Example: The following example register a `POST` that  made on `http://somewhere.org`, with the body `"some body"` that will return a `OK` status code with . The call to `restTemplate`must be a `GET` on the matching `url`. The call will return `"some body"` as a response.

```java
restTemplate.setRequestFactory(clientHttpRequestFactory);
clientHttpRequestFactory.register(
		Context.builder()
				.url("http://somewhere.org")
				.statusCode(HttpStatus.OK)
				.method(HttpMethod.POST)
				.requestPayload(Payload.builder()
						.type(Payload.Type.RAW_STRING)
						.value("some body")
						.build())
                .requestPayload(Payload.builder()
                        .type(Payload.Type.CLASSPATH_RESOURCE)
                        .value("fixtures/POST.json")
                        .build())
				.build()
);
// use restTemplate
```

## Add to your project

Add the dependancy to your project by using [jitpack](https://jitpack.io):

```xml
<dependencies>
	...
	<dependency>
	    <groupId>com.github.antechrestos</groupId>
	    <artifactId>rest-client-test</artifactId>
	    <version>TAG|BRANCH|COMMIT</version>
	</dependency>
	...
</dependencies>

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

```