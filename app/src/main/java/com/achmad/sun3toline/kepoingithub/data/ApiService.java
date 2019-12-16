package com.achmad.sun3toline.kepoingithub.data;

import com.achmad.sun3toline.kepoingithub.data.model.Users;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("search/users")
    Observable<Users> requestUsers(@Query("q") String searchUser);

    @GET("search/users")
    Call<Users> requestUsersGithub(@Query("q") String searchUser, @Query("page") int page);
}
