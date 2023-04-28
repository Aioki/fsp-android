package com.aioki.api.client.interceptors;


import android.os.Build;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Adds a custom {@code User-Agent} header to OkHttp requests.
 */
public class UserAgentInterceptor implements Interceptor {

    public final String userAgent;

    public UserAgentInterceptor(){
        this("okhttp", "TEST");
    }

    public UserAgentInterceptor(String appName, String versionName) {
        this.userAgent = String.format(Locale.US,
                "%s/%s (Android %s; %s; %s %s; %s)",
                appName,
                versionName,
                Build.VERSION.RELEASE,
                Build.MODEL,
                Build.BRAND,
                Build.DEVICE,
                Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request userAgentRequest = chain.request()
                .newBuilder()
                .header("User-Agent", userAgent)
                .build();
        return chain.proceed(userAgentRequest);
    }
}