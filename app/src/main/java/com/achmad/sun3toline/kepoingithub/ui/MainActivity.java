package com.achmad.sun3toline.kepoingithub.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.achmad.sun3toline.kepoingithub.R;
import com.achmad.sun3toline.kepoingithub.ui.searchuser.SearchUserFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        FragmentManager mFragmentManager = getSupportFragmentManager();
        SearchUserFragment mSearchFragment = new SearchUserFragment();
        Fragment fragment = mFragmentManager.findFragmentByTag(SearchUserFragment.class.getSimpleName());

        if (!(fragment instanceof SearchUserFragment)) {
            Log.d("SearchUsersFragment", "Fragment name :" + SearchUserFragment.class.getSimpleName());
            mFragmentManager
                    .beginTransaction()
                    .add(R.id.frame_container, mSearchFragment, SearchUserFragment.class.getSimpleName())
                    .commit();
        }

    }
}
