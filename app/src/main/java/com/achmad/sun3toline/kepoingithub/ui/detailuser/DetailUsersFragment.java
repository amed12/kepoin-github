package com.achmad.sun3toline.kepoingithub.ui.detailuser;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.achmad.sun3toline.kepoingithub.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailUsersFragment extends Fragment {


    public DetailUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_users, container, false);
    }

}
