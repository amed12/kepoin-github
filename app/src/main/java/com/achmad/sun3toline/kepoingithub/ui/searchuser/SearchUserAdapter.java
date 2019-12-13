package com.achmad.sun3toline.kepoingithub.ui.searchuser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.achmad.sun3toline.kepoingithub.R;
import com.achmad.sun3toline.kepoingithub.data.model.UsersResponse;
import com.achmad.sun3toline.kepoingithub.utils.BaseViewHolder;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.achmad.sun3toline.kepoingithub.utils.Constant.VIEW_TYPE_EMPTY;
import static com.achmad.sun3toline.kepoingithub.utils.Constant.VIEW_TYPE_NORMAL;

public class SearchUserAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<UsersResponse> mValues;

    SearchUserAdapter() {
    }

    void addItems(List<UsersResponse> mValues) {
        this.mValues = mValues;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_fragment_search_user, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_empty, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (mValues != null && mValues.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mValues != null && mValues.size() > 0) {
            return mValues.size();
        } else {
            return 1;
        }
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.img_user)
        ImageView imgUser;

        @BindView(R.id.txt_username)
        TextView tvUser;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            final UsersResponse users = mValues.get(position);
            tvUser.setText(users.getLogin());
            Glide.with(itemView.getContext())
                    .load(users.getAvatarUrl())
                    .into(imgUser);
        }

        @Override
        public int getCurrentPosition() {
            return super.getCurrentPosition();
        }

        @Override
        protected void clear() {
        }
    }

    public class EmptyViewHolder extends BaseViewHolder {

        EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
        }

        @Override
        protected void clear() {

        }
    }
}
