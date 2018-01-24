
/* This file is part of Sign-up Page Sample.

The Sign-up Page Sample is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The Sign-up Page Sample is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with The Sign-up Page Sample.  If not, see <http://www.gnu.org/licenses/>. */

package com.trumpia.signup.util;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class RequestRest {

    private String requestUrl;
    private String requestBody;
    private String apiKey;

    public void setRequestUrl(String requestUrl) {

        this.requestUrl = requestUrl;
    }

    public void setRequestBody(String requestBody) {

        this.requestBody = requestBody;
    }

    public void setApiKey(String apiKey) {

        this.apiKey = apiKey;
    }

    public static       OkHttpClient client    = new OkHttpClient();
    public static final MediaType    mediaType = MediaType.parse("application/json");

    public String get() throws IOException {

        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("content-type", "application/json")
                .addHeader("x-apikey", apiKey)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String put() throws IOException {

        RequestBody body = RequestBody.create(mediaType, requestBody);
        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("content-type", "application/json")
                .addHeader("x-apikey", apiKey)
                .put(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();

    }

    public String post() throws IOException {

        RequestBody body = RequestBody.create(mediaType, requestBody);
        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("content-type", "application/json")
                .addHeader("x-apikey", apiKey)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
