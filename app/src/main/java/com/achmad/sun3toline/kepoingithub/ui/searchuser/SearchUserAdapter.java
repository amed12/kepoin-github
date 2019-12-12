package com.achmad.sun3toline.kepoingithub.ui.searchuser;

import android.text.TextUtils;
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
import com.google.android.material.button.MaterialButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.achmad.sun3toline.kepoingithub.utils.Constant.VIEW_TYPE_EMPTY;
import static com.achmad.sun3toline.kepoingithub.utils.Constant.VIEW_TYPE_NORMAL;

public class SearchUserAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private OnClickInteraction mClickInteraction;
    private List<UsersResponse> mValues;
    private String textError;

    SearchUserAdapter(OnClickInteraction onClickInteraction) {
        this.mClickInteraction = onClickInteraction;
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

    private String getTextError() {
        return textError;
    }

    public void setTextError(String textError) {
        this.textError = textError;
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
            final View.OnClickListener onClickListener = view -> mClickInteraction.onItemFragmentClicked(mValues.get(getAdapterPosition()));
            itemView.setOnClickListener(onClickListener);
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
        @BindView(R.id.next_button)
        MaterialButton buttonRetry;

        @BindView(R.id.text_error)
        TextView tvErrorMessage;

        @BindView(R.id.img_alert)
        ImageView imgAlert;

        EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            if (!TextUtils.isEmpty(getTextError())) {
                final View.OnClickListener onClickListener = view -> mClickInteraction.onButtonRetryClicked();
                tvErrorMessage.setText(getTextError());
                buttonRetry.setOnClickListener(onClickListener);
            } else {
                imgAlert.setVisibility(View.GONE);
                tvErrorMessage.setVisibility(View.GONE);
                buttonRetry.setVisibility(View.GONE);
            }
        }

        @Override
        protected void clear() {

        }
    }
}
