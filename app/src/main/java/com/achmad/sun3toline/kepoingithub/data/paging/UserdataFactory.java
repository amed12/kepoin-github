package com.achmad.sun3toline.kepoingithub.data.paging;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.achmad.sun3toline.kepoingithub.data.Repository;
import com.achmad.sun3toline.kepoingithub.data.model.UsersResponse;

import io.reactivex.disposables.CompositeDisposable;

public class UserdataFactory extends DataSource.Factory<Integer, UsersResponse> {
    private MutableLiveData<UsersDataSource> liveData;
    private Repository repository;
    private CompositeDisposable compositeDisposable;
    private String query;

    public UserdataFactory(Repository repository, CompositeDisposable compositeDisposable, String query) {
        this.repository = repository;
        this.compositeDisposable = compositeDisposable;
        this.query = query;
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<UsersDataSource> getLiveData() {
        return liveData;
    }

    @Override
    public DataSource<Integer, UsersResponse> create() {
        UsersDataSource dataSource = new UsersDataSource(repository, compositeDisposable, query);
        liveData.postValue(dataSource);
        return dataSource;
    }
}
