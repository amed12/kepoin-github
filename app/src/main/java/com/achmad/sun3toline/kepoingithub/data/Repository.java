package com.achmad.sun3toline.kepoingithub.data;

import com.achmad.sun3toline.kepoingithub.data.model.Users;

import io.reactivex.Observable;

public class Repository {
    private ApiService usersGithubApi;

    public Repository() {
        usersGithubApi = NetworkConfig.getInstance();
    }
    public Observable<Users> requestUsers(String searchText) {
        return usersGithubApi.requestUsers(searchText);
    }
}
