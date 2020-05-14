package com.example.statsdelivery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by drekerd (Mário Silva) or 13/05/2020
 */
class DivisionRecyclerViewAdapter extends RecyclerView.Adapter<DivisionRecyclerViewAdapter.DivisionImageViewHolder> {
    private static final String TAG = "DivisionRecyclerViewAda";
    private User mUser;
    private Context mContext;

    public DivisionRecyclerViewAdapter(User user, Context context) {
        mUser = user;
        mContext = context;
    }

    @NonNull
    @Override
    public DivisionImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Called by the layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder: new View Requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new DivisionImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DivisionImageViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    static class DivisionImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "DivisionImageViewHolder";
        ImageView mThumbnail = null;
        TextView mUserName = null;

        public DivisionImageViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "DivisionImageViewHolder: Starts");
            this.mThumbnail = itemView.findViewById(R.id.user_avatar);
            this.mUserName = itemView.findViewById(R.id.user_name_textView);
        }
    }
}
