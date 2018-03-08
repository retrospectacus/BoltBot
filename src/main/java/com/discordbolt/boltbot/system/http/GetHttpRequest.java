package com.discordbolt.boltbot.system.http;

public class GetHttpRequest extends HttpRequest {

    protected static abstract class Init<T extends Init<T>> extends HttpRequest.Init<T> {
        public Init() {
            request.GET();
        }
    }

    public static class Builder extends Init<Builder> {
        @Override
        protected Builder self() {
            return this;
        }
    }

    protected GetHttpRequest(Init<?> init) {
        super(init);
    }
}
