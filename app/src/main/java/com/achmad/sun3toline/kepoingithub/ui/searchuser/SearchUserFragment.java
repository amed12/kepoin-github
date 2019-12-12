package com.achmad.sun3toline.kepoingithub.ui.searchuser;


import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.achmad.sun3toline.kepoingithub.R;
import com.achmad.sun3toline.kepoingithub.data.UsersApiResponse;
import com.achmad.sun3toline.kepoingithub.data.model.UsersResponse;
import com.achmad.sun3toline.kepoingithub.utils.CommonUtils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchUserFragment extends Fragment implements SearchView.OnQueryTextListener {
    @BindView(R.id.rv_search)
    RecyclerView rvUsers;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;
    @BindView(R.id.relatifLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.search_view_toolbar)
    SearchView mSearchView;
    private ArrayList<UsersResponse> usersResponses = new ArrayList<>();
    private SearchUserAdapter mAdapter;
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
        ButterKnife.bind(this, Objects.requireNonNull(getActivity()));
        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(getView()).getContext()));
        mAdapter = new SearchUserAdapter();
        rvUsers.setAdapter(mAdapter);
        mViewModel = new ViewModelProvider(this).get(SearchUserViewModel.class);
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
//        mViewModel.initGithubSearch(textSearch);
        mViewModel.setCurrentQueryText(textSearch);
        mViewModel.requestUsers(textSearch);
        mViewModel.getMutableLiveDataUsers().observe(getViewLifecycleOwner(), this::consumeResponse);
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

    private void consumeResponse(UsersApiResponse apiResponse) {

        switch (apiResponse.status) {

            case LOADING:
                mAdapter.setTextError("");
                break;

            case SUCCESS:
                stopAnimationView();
                if (apiResponse.data.getTotalCount() != 0) {
                    usersResponses.addAll(apiResponse.data.getItems());
                    mAdapter.addItems(usersResponses);
                    mAdapter.notifyDataSetChanged();
                } else if (apiResponse.data.getTotalCount() == 0) {
                    showSnackBar(getResources().getString(R.string.value_empty));
                    mAdapter.setTextError(getResources().getString(R.string.value_empty));
                }
                break;

            case ERROR:
                stopAnimationView();
                boolean internetConnected = CommonUtils.checkInternetConnection(Objects.requireNonNull(getActivity()).getApplicationContext());
                if (internetConnected) {
                    HttpException errorBody = (HttpException) apiResponse.error;
                    switch (errorBody.code()) {
                        case 403:
                            showSnackBar(getResources().getString(R.string.error_code_403));
                            break;
                        case 400:
                            showSnackBar(getResources().getString(R.string.error_code_400));
                            break;
                        case 422:
                            showSnackBar(getResources().getString(R.string.error_code_422));
                            break;
                        default:
                            break;
                    }
                } else {
                    showSnackBarButton();
                }
                break;

            default:
                break;
        }
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar
                .make(relativeLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void showSnackBarButton() {
        Snackbar snackbar = Snackbar.make(relativeLayout, getResources().getString(R.string.alert_nointernet), Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED)
                .setAction(getResources().getString(R.string.btn_snackbar_retryconnect), view -> loadUser(mViewModel.getCurrentQueryText()))
                .setDuration(30000);
        TextView tv = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.YELLOW);
        snackbar.show();
    }
}
