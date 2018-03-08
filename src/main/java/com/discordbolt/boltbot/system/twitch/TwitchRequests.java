package com.discordbolt.boltbot.system.twitch;

import com.discordbolt.boltbot.system.http.GetHttpRequest;
import jdk.incubator.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.zip.DataFormatException;

public class TwitchRequests {

    private static final String BASE_URL = "https://api.twitch.tv/";
    private static final String API_VERSION_NEW = "helix/";
    private static final String API_VERSION_OLD = "kraken";

    private static NameValuePair authHeader;

    static {
        try {
            authHeader = TwitchAPI.getInstance().getAuthHeader();
        } catch (DataFormatException e) {
            e.printStackTrace();
        }
    }

    public static String getApiVerrsionNew() {
        return BASE_URL + API_VERSION_NEW;
    }

    public static String getApiVersionOld() {
        return BASE_URL + API_VERSION_OLD;
    }

    public static void testREST() throws DataFormatException, IOException, InterruptedException {
        // Test GET
        HttpResponse<String> response = new GetHttpRequest.Builder().withURL(getApiVersionOld() + "users")
                                                                    .withParameters(new BasicNameValuePair("login", "techtony96"))
                                                                    .withHeaders(new BasicNameValuePair("Accept", "application/vnd.twitchtv.v5+json"))
                                                                    .withHeaders(new BasicNameValuePair("Client-ID", TwitchAPI
                                                                            .getInstance()
                                                                            .getClientID()))
                                                                    .makeRequest();

        System.out.println(response.body());
    }
}
