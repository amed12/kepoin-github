package com.achmad.sun3toline.kepoingithub.data.paging;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.achmad.sun3toline.kepoingithub.data.model.UsersResponse;

public class UserdataFactory extends DataSource.Factory<Integer, UsersResponse> {
    private MutableLiveData<UsersDataSource> liveData;
    private String query;

    public UserdataFactory(String query) {
        this.query = query;
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<UsersDataSource> getLiveData() {
        return liveData;
    }

    @Override
    public DataSource<Integer, UsersResponse> create() {
        UsersDataSource dataSource = new UsersDataSource(query);
        liveData.postValue(dataSource);
        return dataSource;
    }
}
