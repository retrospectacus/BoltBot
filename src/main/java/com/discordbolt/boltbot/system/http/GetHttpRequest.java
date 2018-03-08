package com.discordbolt.boltbot.system.http;

import java.net.URISyntaxException;

public class GetHttpRequest extends HttpClientRequest {

    protected static abstract class Init<T extends Init<T>> extends HttpClientRequest.Init<T> {

        public GetHttpRequest build() throws URISyntaxException {
            return new GetHttpRequest(this);
        }
    }

    public static class Builder extends Init<Builder> {
        @Override
        protected Builder self() {
            return this;
        }
    }

    protected GetHttpRequest(Init<?> init) throws URISyntaxException {
        super(init);
        requestBuilder.GET();
    }
}
