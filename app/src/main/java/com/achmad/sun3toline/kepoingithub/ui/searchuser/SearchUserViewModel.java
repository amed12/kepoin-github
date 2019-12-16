package com.achmad.sun3toline.kepoingithub.ui.searchuser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.achmad.sun3toline.kepoingithub.data.Repository;
import com.achmad.sun3toline.kepoingithub.data.UsersApiResponse;
import com.achmad.sun3toline.kepoingithub.data.model.StatusHandling;
import com.achmad.sun3toline.kepoingithub.data.model.Users;
import com.achmad.sun3toline.kepoingithub.data.model.UsersResponse;
import com.achmad.sun3toline.kepoingithub.data.paging.UserdataFactory;
import com.achmad.sun3toline.kepoingithub.data.paging.UsersDataSource;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SearchUserViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<Users> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<UsersApiResponse> mutableLiveDataUsers = new MutableLiveData<>();

    private LiveData<UsersApiResponse> mutableLiveDataUsersGithub;
    private LiveData<StatusHandling> statusHandlingLiveData;
    private LiveData<PagedList<UsersResponse>> listLiveData;
    private UserdataFactory userdataFactory;

    public LiveData<UsersApiResponse> getMutableLiveDataUsersGithub() {
        return mutableLiveDataUsersGithub;
    }

    LiveData<StatusHandling> getStatusHandlingLiveData() {
        return statusHandlingLiveData;
    }
    private Repository mRepository;
    private String currentQueryText;

    LiveData<PagedList<UsersResponse>> getListLiveData() {
        return listLiveData;
    }

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

    void initializePaging(String searchText) {
        userdataFactory = new UserdataFactory(mRepository, disposables, searchText);
        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(true)
                        .setPageSize(10).build();

        listLiveData = new LivePagedListBuilder<>(userdataFactory, pagedListConfig)
                .build();

        mutableLiveDataUsersGithub = Transformations.switchMap(userdataFactory.getLiveData(), UsersDataSource::getUsersApiResponseMutableLiveData);
        statusHandlingLiveData = Transformations.switchMap(userdataFactory.getLiveData(), UsersDataSource::getLiveStatus);
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

    MutableLiveData<UsersApiResponse> getMutableLiveDataUsers() {
        return mutableLiveDataUsers;
    }
}
