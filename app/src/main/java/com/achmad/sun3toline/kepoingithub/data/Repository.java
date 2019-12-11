package com.achmad.sun3toline.kepoingithub.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.achmad.sun3toline.kepoingithub.data.model.Users;

import java.util.List;

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
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    usersGithub.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                usersGithub.setValue(null);
            }
        });
        return usersGithub;
    }
}
