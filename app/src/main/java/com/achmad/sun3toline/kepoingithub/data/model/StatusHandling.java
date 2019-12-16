package com.achmad.sun3toline.kepoingithub.data.model;

import com.achmad.sun3toline.kepoingithub.utils.Status;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

import static com.achmad.sun3toline.kepoingithub.utils.Status.ERROR;
import static com.achmad.sun3toline.kepoingithub.utils.Status.LOADING;
import static com.achmad.sun3toline.kepoingithub.utils.Status.SUCCESS;

public class StatusHandling {
    public final Status status;


    @Nullable
    public final Throwable error;

    private StatusHandling(Status status, Throwable error) {
        this.status = status;
        this.error = error;
    }

    public static StatusHandling loading() {
        return new StatusHandling(LOADING, null);
    }

    public static StatusHandling success() {
        return new StatusHandling(SUCCESS, null);
    }

    public static StatusHandling error(@NonNull Throwable error) {
        return new StatusHandling(ERROR, error);
    }
}
