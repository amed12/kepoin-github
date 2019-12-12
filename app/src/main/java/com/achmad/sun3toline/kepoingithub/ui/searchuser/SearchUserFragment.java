package com.achmad.sun3toline.kepoingithub.ui.searchuser;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.achmad.sun3toline.kepoingithub.R;
import com.achmad.sun3toline.kepoingithub.data.model.UsersResponse;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchUserFragment extends Fragment implements OnClickInteraction, SearchView.OnQueryTextListener {
    private RecyclerView rvUsers;
    private ShimmerFrameLayout mShimmerViewContainer;
    private ArrayList<UsersResponse> usersResponses = new ArrayList<>();
    private SearchUserAdapter mAdapter;
    private SearchView mSearchView;
    private SearchUserViewModel mViewModel;
    private String mCurFilter;

    public SearchUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_user_github, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        rvUsers = Objects.requireNonNull(getActivity()).findViewById(R.id.rv_search);
        mShimmerViewContainer = getActivity().findViewById(R.id.shimmer_view_container);
        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(getView()).getContext()));
        mAdapter = new SearchUserAdapter(this);
        rvUsers.setAdapter(mAdapter);
        mViewModel = new ViewModelProvider(this).get(SearchUserViewModel.class);
        mSearchView = getActivity().findViewById(R.id.search_view_toolbar);
        EditText editText = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.primary_text));
        mSearchView.setOnQueryTextFocusChangeListener((view, focused) -> {
            if (!focused) {
                if (TextUtils.isEmpty(editText.getText())) {
                    usersResponses.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public void onItemFragmentClicked(UsersResponse usersResponse) {

    }

    @Override
    public void onButtonRetryClicked() {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        loadUser(query);
        return true;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
        if (mCurFilter == null && newFilter == null) {
            return true;
        }
        if (mCurFilter != null && mCurFilter.equals(newFilter)) {
            return true;
        }
        mCurFilter = newFilter;
        loadUser(mCurFilter);
        return true;
    }

    private void loadUser(String textSearch) {
        if (!TextUtils.isEmpty(textSearch)) {
            showAnimationView();
        } else {
            stopAnimationView();
        }
        mViewModel.initGithubSearch(textSearch);
        mViewModel.getUsersRepository().observe(getViewLifecycleOwner(), users -> {
            if (users.getItems().size() > 0) {
                usersResponses.addAll(users.getItems());
                mAdapter.addItems(usersResponses);
                mAdapter.notifyDataSetChanged();
                stopAnimationView();
            }
        });
    }

    private void stopAnimationView() {
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);
    }

    private void showAnimationView() {
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        usersResponses.clear();
        mAdapter.notifyDataSetChanged();
        mShimmerViewContainer.startShimmerAnimation();
    }
}
