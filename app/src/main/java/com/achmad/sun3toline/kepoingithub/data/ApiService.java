package com.achmad.sun3toline.kepoingithub.data;

import com.achmad.sun3toline.kepoingithub.data.model.DetailUsers;
import com.achmad.sun3toline.kepoingithub.data.model.Users;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("search/users")
    Call<Users> getUser(@Query("q") String searchUser);

    @GET("users")
    Call<DetailUsers> getUserDetail(String user);
}
