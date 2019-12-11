package com.achmad.sun3toline.kepoingithub.ui.searchuser;

import com.achmad.sun3toline.kepoingithub.data.model.UsersResponse;

public interface OnClickInteraction {
    void onItemFragmentClicked(UsersResponse usersResponse);

    void onButtonRetryClicked();
}
