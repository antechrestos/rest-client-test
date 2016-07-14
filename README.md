# Rest Client Test

## Description

This project brings you a simple library to mock spring rest template in order to test your client.

Example: The following example registers a `POST` made on `http://somewhere.org`, with the request body `"some body"`. This call will return a `OK` status code with the content of the file found in classpath `fixtures/POST.json`.

The call to `restTemplate` must be a `POST` on the matching `url`. The call will check that the body submitted matched the one given at context registration.

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

###Query parameters

Query parameters may either be specified in the `url`creation paramers, or by using the `builder` method `queryParameter`. If both way are used, the checked query parameters will be a merge of the two methods.
They are checked when the call is made.

### Request headers
Headers specified by the `builder` method `header` are checked when the call is made.

### Request payload
A check on the body that is sent can be made. You may either specify a `Payload.Type.RAW_STRING` or a `Payload.Type.CLASSPATH_RESOURCE`where the content will be loaded and compared to the sent body.

### Status code
The status code that will be returned

### Response headers
The headers that will be sent by the call.

### Response payload
The response body returned. You may either specify a `Payload.Type.RAW_STRING` or a `Payload.Type.CLASSPATH_RESOURCE` where the content will be loaded and sent as a body.




## Add to your project

Add the dependency to your project by using maven:

```xml
<dependency>
    <groupId>com.github.antechrestos</groupId>
    <artifactId>restclienttest</artifactId>
    <version>1.0.0</version>
</dependency>

```