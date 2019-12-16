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
import com.achmad.sun3toline.kepoingithub.data.model.StatusHandling;
import com.achmad.sun3toline.kepoingithub.utils.CommonUtils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.HttpException;

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
    private SearchUserViewModel mViewModel;
    private String mCurFilter;
    private ItemSearchAdapter adapter;

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
        adapter = new ItemSearchAdapter();
        mViewModel = new ViewModelProvider(this).get(SearchUserViewModel.class);
        EditText editText = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.primary_text));
        mSearchView.setOnQueryTextFocusChangeListener((view, focused) -> {
            if (!focused) {
                if (TextUtils.isEmpty(editText.getText())) {
                    adapter.notifyDataSetChanged();
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
        mViewModel.setCurrentQueryText(textSearch);
        mViewModel.initializePaging(textSearch);
        mViewModel.getListLiveData().observe(getViewLifecycleOwner(), pagedList -> adapter.submitList(pagedList)
        );
        mViewModel.getStatusHandlingLiveData().observe(getViewLifecycleOwner(), this::consumeResponseStatus);
        rvUsers.setAdapter(adapter);
    }

    private void stopAnimationView() {
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);
    }

    private void showAnimationView() {
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
        mShimmerViewContainer.startShimmerAnimation();
    }

    private void consumeResponseStatus(StatusHandling apiResponse) {
        switch (apiResponse.status) {

            case LOADING:
                if (!TextUtils.isEmpty(mViewModel.getCurrentQueryText())) {
                    showAnimationView();
                } else {
                    stopAnimationView();
                }
                break;
            case SUCCESS:
                stopAnimationView();
                if (Objects.requireNonNull(adapter.getCurrentList()).size() == 0) {
                    showSnackBar(getResources().getString(R.string.value_empty));
                }
                break;
            case ERROR:
                stopAnimationView();
                Throwable error = apiResponse.error;
                boolean internetConnected = CommonUtils.checkInternetConnection(Objects.requireNonNull(getActivity()).getApplicationContext());
                if (internetConnected) {
                    if (error instanceof SocketTimeoutException) {
                        showSnackBarButton(getResources().getString(R.string.alert_slow_internet)); // "Connection Timeout"
                    } else if (error instanceof IOException) {
                        showSnackBarButton(getResources().getString(R.string.alert_nointernet));
                    } else
//                        if (error != null)
                    {
                        if (((HttpException) error).code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                            showSnackBarButton(getResources().getString(R.string.alert_nointernet));
                        } else if (((HttpException) error).code() == HttpURLConnection.HTTP_FORBIDDEN) {
                            showSnackBar(getResources().getString(R.string.error_code_403));
                        } else if (((HttpException) error).code() == 422) {
                            showSnackBar(getResources().getString(R.string.error_code_422));
                        } else {
                            showSnackBar(apiResponse.error.getMessage());
                        }
                    }
                } else {
                    showSnackBarButton(getResources().getString(R.string.alert_nointernet));
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

    private void showSnackBarButton(String message) {
        Snackbar snackbar = Snackbar.make(relativeLayout, message, Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED)
                .setAction(getResources().getString(R.string.btn_snackbar_retryconnect), view -> loadUser(mViewModel.getCurrentQueryText()))
                .setDuration(30000);
        TextView tv = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.YELLOW);
        snackbar.show();
    }
}
