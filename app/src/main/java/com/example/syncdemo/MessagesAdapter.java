package com.example.syncdemo;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.syncdemo.persistence.SyncMessage;
import com.example.syncdemo.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private List<SyncMessage> messageResults = new ArrayList<SyncMessage>();

    public void setMessageResults(List<SyncMessage> results) {
        this.messageResults = results;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.message)
        TextView messageView;

        @Bind(R.id.name)
        TextView nameView;

        @Bind(R.id.avatar_image_view)
        ImageView imageView;

        @Bind(R.id.message_time)
        TextView messageTimestampView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SyncMessage message = messageResults.get(position);

        holder.messageView.setText(message.getMessage());
        holder.nameView.setText(message.getUserName());

        CharSequence time = DateUtils.getRelativeDateTimeString(holder.messageTimestampView.getContext(),
                message.getTime()*1000,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.DAY_IN_MILLIS,
                0
        );

        holder.messageTimestampView.setText(time);

        Picasso.with(holder.imageView.getContext())
                .load(message.getUserAvatarUrl())
                .transform(new CircleTransform())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return messageResults.size();
    }

}
