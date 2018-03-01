package com.discordbolt.boltbot.system.twitch;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class TwitchRequests {

    private static final String BASE_URL = "https://api.twitch.tv/";
    private static final String API_VERSION_NEW = "helix/";
    private static final String API_VERSION_OLD = "kraken";

    private static final HttpClient client = HttpClient.newHttpClient();

    public static String getApiVerrsionNew() {
        return BASE_URL + API_VERSION_NEW;
    }

    public static String getApiVersionOld() {
        return BASE_URL + API_VERSION_OLD;
    }

    private static URI buildURI(String url, List<NameValuePair> parameters) throws URISyntaxException {
        URIBuilder uri = new URIBuilder(url).addParameters(parameters);
    }

    public static void get(String url, List<NameValuePair> parameters, List<NameValuePair> headers) throws URISyntaxException {
        if (url == null || url.length() <= 0)
            throw new IllegalArgumentException("'" + (url != null ? url : "url") + "' is not a valid URL for GET request");

        HttpRequest.Builder request = HttpRequest.newBuilder(buildURI(url, parameters));
        //TODO add headers
        //todo only add parameters if not null?? Check if the jdk will just ignore it if its null
    }
}
