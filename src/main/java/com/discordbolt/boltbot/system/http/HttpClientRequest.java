package com.discordbolt.boltbot.system.http;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class HttpClientRequest {

    private static final HttpClient universalClient = HttpClient.newHttpClient();

    private HttpClient client;
    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

    protected static abstract class Init<T extends Init<T>> {

        boolean usePrivateClient;
        String url;
        List<NameValuePair> parameters = new ArrayList<>();
        List<NameValuePair> headers = new ArrayList<>();

        protected abstract T self();

        public T usePrivateClient() {
            usePrivateClient = true;
            return self();
        }

        public T withURL(String url) {
            this.url = url;
            return self();
        }

        public T addParameters(NameValuePair... parameters) {
            Collections.addAll(this.parameters, parameters);
            return self();
        }

        public T addParameters(List<NameValuePair> parameters) {
            this.parameters.addAll(parameters);
            return self();
        }

        public T addHeaders(NameValuePair... headers) {
            Collections.addAll(this.headers, headers);
            return self();
        }

        public T addHeaders(List<NameValuePair> headers) {
            this.headers.addAll(headers);
            return self();
        }
    }

    public static class Builder extends Init<Builder> {
        @Override
        protected Builder self() {
            return this;
        }
    }

    HttpClientRequest(Init<?> init) throws URISyntaxException {
        client = init.usePrivateClient ? HttpClient.newHttpClient() : universalClient;

        requestBuilder.uri(new URIBuilder(init.url).addParameters(init.parameters).build());
        init.headers.forEach(h -> requestBuilder.header(h.getName(), h.getValue()));
    }

    public HttpResponse<String> makeRequest() throws IOException, InterruptedException {
        return client.send(requestBuilder.build(), HttpResponse.BodyHandler.asString());
    }
}
