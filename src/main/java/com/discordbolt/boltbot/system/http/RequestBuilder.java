package com.discordbolt.boltbot.system.http;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import org.apache.http.NameValuePair;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

public abstract class RequestBuilder {

    private static final HttpClient universalClient = HttpClient.newHttpClient();

    private final HttpClient client;
    final HttpRequest.Builder request = HttpRequest.newBuilder();

    RequestBuilder(boolean privateClient) {
        if (privateClient)
            client = HttpClient.newHttpClient();
        else
            client = universalClient;
    }

    public RequestBuilder withURL(String url) {
        if (url == null || url.length() <= 0)
            throw new IllegalArgumentException("'" + url + "' is not a valid URL for GET request");

        request.uri(URI.create(url));
        return this;
    }

    public RequestBuilder withParameters(NameValuePair... parameters) {
        //TODO how to handle if URL is not already set?
        return this;
    }

    public RequestBuilder withHeaders(NameValuePair... headers) {
        Arrays.stream(headers).forEach(h -> request.header(h.getName(), h.getValue()));
        return this;
    }

    public HttpResponse<String> makeRequest() throws IOException, InterruptedException {
        return client.send(request.build(), HttpResponse.BodyHandler.asString());
    }
}
