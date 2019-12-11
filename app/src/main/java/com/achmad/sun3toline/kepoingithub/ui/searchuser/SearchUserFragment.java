package com.achmad.sun3toline.kepoingithub.ui.searchuser;


import android.content.Context;
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

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchUserFragment extends Fragment implements OnClickInteraction, SearchView.OnQueryTextListener {
    RecyclerView rvUsers;
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
        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(getView()).getContext()));
        mAdapter = new SearchUserAdapter(this);
        rvUsers.setAdapter(mAdapter);
        mViewModel = new ViewModelProvider(this).get(SearchUserViewModel.class);
        mSearchView = getActivity().findViewById(R.id.search_view_toolbar);
        EditText editText = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.primary_text));
        mSearchView.setOnQueryTextListener(this);

    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
//        inflater.inflate(R.menu.search_menu, menu);
//        MenuItem item = menu.findItem(R.id.action_search);
//        mSearchView = new MySearchView(getActivity());
//        if (item != null) {
//            mSearchView = (SearchView) item.getActionView();
//            mSearchView.setBackgroundColor(getResources().getColor(R.color.icons));
//            ImageView closeBtn = mSearchView.findViewById(R.id.search_close_btn);
//            EditText editText = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
//            editText.setTextColor(getResources().getColor(R.color.primary_text));
//            closeBtn.setEnabled(false);
//            closeBtn.setImageDrawable(null);
//            item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//                @Override
//                public boolean onMenuItemActionExpand(MenuItem menuItem) {
//                    return true;
//                }
//
//                @Override
//                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
//                    usersResponses.clear();
//                    mAdapter.notifyDataSetChanged();
//                    return true;
//                }
//            });
//        }
//        mSearchView.setQueryHint(Html.fromHtml("<font color = #757575>" + "Search Github users" + "</font>"));
//        mSearchView.setOnQueryTextListener(this);
//        mSearchView.setIconifiedByDefault(false);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

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
        // Don't do anything if the filter hasn't actually changed.
        // Prevents restarting the loader when restoring state.
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
        mViewModel.initGithubSearch(textSearch);
        mViewModel.getUsersRepository().observe(getViewLifecycleOwner(), users -> {
            usersResponses.clear();
            mAdapter.notifyDataSetChanged();
            usersResponses.addAll(users.getItems());
            mAdapter.addItems(usersResponses);
            mAdapter.notifyDataSetChanged();
        });
    }


    public static class MySearchView extends SearchView {
        public MySearchView(Context context) {
            super(context);
            SearchView searchView = findViewById(R.id.search_view_toolbar);
        }

        @Override
        public void onActionViewCollapsed() {
            setQuery("", false);
            super.onActionViewCollapsed();
        }
    }
}
