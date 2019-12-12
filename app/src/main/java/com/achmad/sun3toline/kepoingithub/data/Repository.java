package com.achmad.sun3toline.kepoingithub.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.achmad.sun3toline.kepoingithub.data.model.Users;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private LiveData<List<Users>> mAllUsers;
    private MutableLiveData<Integer> errorCode = new MutableLiveData<>();
    private ApiService usersGithubApi;

    public Repository() {
        usersGithubApi = NetworkConfig.getInstance();
    }

    public MutableLiveData<Users> getUsersFromSearch(String searchText) {
        final MutableLiveData<Users> usersGithub = new MutableLiveData<>();
        usersGithubApi.getUser(searchText).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(@NotNull Call<Users> call, @NotNull Response<Users> response) {
                try {
                    if (response.isSuccessful()) {
                        usersGithub.setValue(response.body());
                    }
                } catch (Throwable throwable) {
                    Log.d("errorGetData", Objects.requireNonNull(throwable.getMessage()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<Users> call, @NotNull Throwable t) {
                usersGithub.setValue(null);
            }
        });
        return usersGithub;
    }

    public Observable<Users> requestUsers(String searchText) {
        return usersGithubApi.requestUsers(searchText);
    }
}
