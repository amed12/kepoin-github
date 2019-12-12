package com.achmad.sun3toline.kepoingithub.data;

import com.achmad.sun3toline.kepoingithub.data.model.Users;
import com.achmad.sun3toline.kepoingithub.utils.Status;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

import static com.achmad.sun3toline.kepoingithub.utils.Status.ERROR;
import static com.achmad.sun3toline.kepoingithub.utils.Status.LOADING;
import static com.achmad.sun3toline.kepoingithub.utils.Status.SUCCESS;

public class UsersApiResponse {
    public final Status status;

    @Nullable
    public final Users data;

    @Nullable
    public final Throwable error;

    private UsersApiResponse(Status status, Users data, Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static UsersApiResponse loading() {
        return new UsersApiResponse(LOADING, null, null);
    }

    public static UsersApiResponse success(@NonNull Users data) {
        return new UsersApiResponse(SUCCESS, data, null);
    }

    public static UsersApiResponse error(@NonNull Throwable error) {
        return new UsersApiResponse(ERROR, null, error);
    }
}
