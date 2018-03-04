package com.discordbolt.boltbot.system.http;

public class GetRequestBuilder extends RequestBuilder {

    public GetRequestBuilder(boolean privateClient) {
        super(privateClient);
        request.GET();
    }

    public GetRequestBuilder() {
        this(false);
    }

    public GetRequestBuilder personal() {
        return this;
    }
}
