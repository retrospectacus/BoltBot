package com.discordbolt.boltbot.system.http;

import jdk.incubator.http.HttpResponse;

public class PostHttpRequest extends HttpRequest {

    protected static abstract class Init<T extends Init<T>> extends HttpRequest.Init<T> {

        public Init() {
            request.POST(null);
        }

        public T withBody(jdk.incubator.http.HttpRequest.BodyProcessor body) {
            request.POST(body);
            return self();
        }

        public T withBody(String body) {
            request.POST(jdk.incubator.http.HttpRequest.BodyProcessor.fromString(body));
            return self();
        }

        @Override
        public HttpResponse<String> makeRequest() {
            return null;
        }
    }

    public static class Builder extends Init<Builder> {
        @Override
        protected Builder self() {
            return this;
        }
    }

    protected PostHttpRequest(Init<?> init) {
        super(init);
    }
}
