package com.achmad.sun3toline.kepoingithub.ui.searchuser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.achmad.sun3toline.kepoingithub.data.Repository;
import com.achmad.sun3toline.kepoingithub.data.model.Users;
import com.achmad.sun3toline.kepoingithub.data.model.UsersResponse;

import java.util.List;

public class SearchUserViewModel extends ViewModel {
    private MutableLiveData<Users> mutableLiveData;
    private LiveData<List<UsersResponse>> usersGithubData;
    private Repository mRepository;

    public SearchUserViewModel() {
        mRepository = new Repository();
    }

    void initGithubSearch(String textSearch) {
        mutableLiveData = mRepository.getUsersFromSearch(textSearch);
    }

    LiveData<Users> getUsersRepository() {
        return mutableLiveData;
    }
}
