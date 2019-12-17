package com.achmad.sun3toline.kepoingithub.ui.searchuser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.achmad.sun3toline.kepoingithub.data.model.StatusHandling;
import com.achmad.sun3toline.kepoingithub.data.model.UsersResponse;
import com.achmad.sun3toline.kepoingithub.data.paging.UserdataFactory;
import com.achmad.sun3toline.kepoingithub.data.paging.UsersDataSource;

public class SearchUserViewModel extends ViewModel {
    private LiveData<StatusHandling> statusHandlingLiveData;
    private LiveData<PagedList<UsersResponse>> listLiveData;

    LiveData<StatusHandling> getStatusHandlingLiveData() {
        return statusHandlingLiveData;
    }
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

    void initializePaging(String searchText) {
        UserdataFactory userdataFactory = new UserdataFactory(searchText);
        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(true)
                        .setPageSize(10).build();

        listLiveData = new LivePagedListBuilder<>(userdataFactory, pagedListConfig)
                .build();

        statusHandlingLiveData = Transformations.switchMap(userdataFactory.getLiveData(), UsersDataSource::getLiveStatus);
    }

}
