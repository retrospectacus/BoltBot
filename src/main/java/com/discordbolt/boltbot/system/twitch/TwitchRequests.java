package com.discordbolt.boltbot.system.twitch;

import com.discordbolt.boltbot.system.http.GetRequestBuilder;
import jdk.incubator.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;

public class TwitchRequests {

    private static final String BASE_URL = "https://api.twitch.tv/";
    private static final String API_VERSION_NEW = "helix/";
    private static final String API_VERSION_OLD = "kraken";



    public static String getApiVerrsionNew() {
        return BASE_URL + API_VERSION_NEW;
    }

    public static String getApiVersionOld() {
        return BASE_URL + API_VERSION_OLD;
    }

    private void test() {
        HttpResponse<String> response = new GetRequestBuilder().withURL(getApiVerrsionNew()).withParameters(new BasicNameValuePair("Authorization", "Bearer %s")).personal();
    }
}
