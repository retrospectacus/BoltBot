package com.discordbolt.boltbot.system.http;

import jdk.incubator.http.HttpRequest;

import java.net.URISyntaxException;

public class PostHttpRequest extends HttpClientRequest {

    protected static abstract class Init<T extends Init<T>> extends HttpClientRequest.Init<T> {
        private String body;

        public T withBody(String body) {
            this.body = body;
            return self();
        }

        public PostHttpRequest build() throws URISyntaxException {
            return new PostHttpRequest(this);
        }
    }

    public static class Builder extends Init<Builder> {
        @Override
        protected Builder self() {
            return this;
        }
    }

    protected PostHttpRequest(Init<?> init) throws URISyntaxException {
        super(init);
        requestBuilder.POST(HttpRequest.BodyProcessor.fromString(init.body));
    }
}
