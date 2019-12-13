package com.achmad.sun3toline.kepoingithub.data;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.achmad.sun3toline.kepoingithub.utils.Constant.BASE_URL;

class NetworkConfig {
    private static final OkHttpClient client;
    private static final Object lockObject = new Object();
    private static ApiService INSTANCE;

    static {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS) // write timeout
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
    }

    static ApiService getInstance() {
        synchronized (lockObject) {
            if (INSTANCE == null) {
                INSTANCE = getRetrofitInstance().create(ApiService.class);
            }
            return INSTANCE;
        }
    }

    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }
}
