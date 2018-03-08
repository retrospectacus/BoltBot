package com.discordbolt.boltbot.system.http;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

abstract class HttpRequest {

    private static final HttpClient universalClient = HttpClient.newHttpClient();

    protected static abstract class Init<T extends Init<T>> {

        private HttpClient client = universalClient;
        jdk.incubator.http.HttpRequest.Builder request = jdk.incubator.http.HttpRequest.newBuilder();

        private URI currentURI;

        protected abstract T self();

        public T usePrivateClient() {
            this.client = HttpClient.newHttpClient();
            return self();
        }

        public T withURL(String url) {
            request.uri(URI.create(url));
            return self();
        }

        public T addParameters(NameValuePair... parameters) {
            request.
                    this.parameters.addAll(Arrays.asList(parameters));
            request.uri(new URIBuilder(request.).addParameters((this.parameters)).build());

            return self();
        }

        public T withHeaders(NameValuePair... headers) {

            return self();
        }

        public HttpResponse<String> makeRequest() throws IOException, InterruptedException {
            return client.send(request.build(), HttpResponse.BodyHandler.asString());
        }

        private URI regenerateURI() {

        }
    }

    public static class Builder extends Init<Builder> {
        @Override
        protected Builder self() {
            return this;
        }
    }

    protected HttpRequest(Init<?> init) {
    }
}
