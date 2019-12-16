package com.achmad.sun3toline.kepoingithub.ui.searchuser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.achmad.sun3toline.kepoingithub.R;
import com.achmad.sun3toline.kepoingithub.data.model.UsersResponse;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.achmad.sun3toline.kepoingithub.data.model.UsersResponse.DIFF_CALLBACK;

public class ItemSearchAdapter extends PagedListAdapter<UsersResponse, ItemSearchAdapter.ViewHolder> {

    ItemSearchAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_search_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsersResponse items = getItem(position);
        if (items != null) {
            holder.tvUser.setText(items.getLogin());
            Glide.with(holder.itemView.getContext())
                    .load(items.getAvatarUrl())
                    .into(holder.imgUser);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_username)
        TextView tvUser;
        @BindView(R.id.img_user)
        ImageView imgUser;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
