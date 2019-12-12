package com.achmad.sun3toline.kepoingithub.ui.searchuser;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.achmad.sun3toline.kepoingithub.data.Repository;
import com.achmad.sun3toline.kepoingithub.data.UsersApiResponse;
import com.achmad.sun3toline.kepoingithub.data.model.Users;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SearchUserViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<Users> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<UsersApiResponse> mutableLiveDataUsers = new MutableLiveData<>();
    private Repository mRepository;
    private String currentQueryText;

    String getCurrentQueryText() {
        return currentQueryText;
    }

    void setCurrentQueryText(String currentQueryText) {
        this.currentQueryText = currentQueryText;
    }

    public SearchUserViewModel() {
        mRepository = new Repository();
    }

    void requestUsers(String searchText) {
        disposables.add(mRepository.requestUsers(searchText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mutableLiveDataUsers.setValue(UsersApiResponse.loading()))
                .subscribe(
                        result -> mutableLiveDataUsers.setValue(UsersApiResponse.success(result)),
                        throwable -> mutableLiveDataUsers.setValue(UsersApiResponse.error(throwable))
                ));
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

    MutableLiveData<UsersApiResponse> getMutableLiveDataUsers() {
        return mutableLiveDataUsers;
    }
}
