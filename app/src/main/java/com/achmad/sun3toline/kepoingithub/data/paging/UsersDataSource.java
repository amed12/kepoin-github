package com.achmad.sun3toline.kepoingithub.data.paging;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.achmad.sun3toline.kepoingithub.data.NetworkConfig;
import com.achmad.sun3toline.kepoingithub.data.model.StatusHandling;
import com.achmad.sun3toline.kepoingithub.data.model.Users;
import com.achmad.sun3toline.kepoingithub.data.model.UsersResponse;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class UsersDataSource extends PageKeyedDataSource<Integer, UsersResponse> {

    private static final int FIRST_PAGE = 1;
    private MutableLiveData<StatusHandling> liveStatus;
    private String queryText;

    UsersDataSource(String queryText) {
        this.queryText = queryText;
        liveStatus = new MutableLiveData<>();
    }

    public MutableLiveData<StatusHandling> getLiveStatus() {
        return liveStatus;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, UsersResponse> callback) {
        liveStatus.postValue(StatusHandling.loading());
        NetworkConfig.getInstance().requestUsersGithub(queryText, FIRST_PAGE)
                .enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(@NotNull Call<Users> call, @NotNull Response<Users> response) {
                        try {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    callback.onResult(response.body().getItems(), null, FIRST_PAGE + 1);
                                    liveStatus.postValue(StatusHandling.success());
                                } else {
                                    onFailure(call, new HttpException(response));
                                }
                            } else {
                                onFailure(call, new HttpException(response));
                            }

                        } catch (Throwable e) {
                            liveStatus.postValue(StatusHandling.error(e));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Users> call, @NotNull Throwable t) {
                        liveStatus.postValue(StatusHandling.error(t));
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, UsersResponse> callback) {
        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
        liveStatus.postValue(StatusHandling.loading());
        NetworkConfig.getInstance().requestUsersGithub(queryText, params.key)
                .enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(@NotNull Call<Users> call, @NotNull Response<Users> response) {
                        try {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    callback.onResult(response.body().getItems(), adjacentKey);
                                    liveStatus.postValue(StatusHandling.success());
                                } else {
                                    onFailure(call, new HttpException(response));
                                }
                            } else {
                                onFailure(call, new HttpException(response));
                            }
                        } catch (Throwable e) {
                            liveStatus.postValue(StatusHandling.error(e));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Users> call, @NotNull Throwable t) {
                        liveStatus.postValue(StatusHandling.error(t));
                    }
                });
    }


    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, UsersResponse> callback) {
        liveStatus.postValue(StatusHandling.loading());
        NetworkConfig.getInstance().requestUsersGithub(queryText, params.key)
                .enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(@NotNull Call<Users> call, @NotNull Response<Users> response) {
                        try {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    Integer key = response.body().getTotalCount() > 0 ? params.key + 1 : null;
                                    callback.onResult(response.body().getItems(), key);
                                    liveStatus.postValue(StatusHandling.success());
                                } else {
                                    onFailure(call, new HttpException(response));
                                }
                            } else {
                                onFailure(call, new HttpException(response));
                            }
                        } catch (Throwable e) {
                            liveStatus.postValue(StatusHandling.error(e));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Users> call, @NotNull Throwable t) {
                        liveStatus.postValue(StatusHandling.error(t));
                    }
                });
    }


}
