package com.example.chatbot;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.MessageHolder>{
    private final String TAG = "MessageAdaper";
    private Context context;
    String userId;
    StorageReference storageReference;
    private RequestOptions requestOptions = new RequestOptions();
    private final int MESSAGE_IN_VIEW_TYPE  = 1;
    private final int MESSAGE_OUT_VIEW_TYPE = 2;





    public MessageAdapter(MainActivity context, com.google.firebase.firestore.Query query, String userId) {
        super(new  FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build());
        this.context = context;
        this.userId = userId;
        requestOptions.placeholder(R.drawable.ic_launcher_foreground);
        storageReference = FirebaseStorage.getInstance().getReference()
                .child("profile_images");
    }


    @Override
    public int getItemViewType(int position) {
        //if message userId matches current userid, set view type 1 else set view type 2
       // if(getItem(position).getMessageUserId().equals(userId)){
            return MESSAGE_IN_VIEW_TYPE;
       // }
       // return MESSAGE_IN_VIEW_TYPE;
    }

    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*
        We're using two different layouts. One for messages from others and the other for user's messages
         */
        View view = null;
        if(viewType==MESSAGE_IN_VIEW_TYPE){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mssg, parent, false);
        }
        else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.msg_out, parent, false);
        }
        return new MessageHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageHolder holder, int position, @NonNull Message model) {
        //Bind values from Message to the viewHolder

        final TextView mText = holder.mText;
        final TextView mUsername = holder.mUsername;
        final TextView mTime = holder.mTime;
        final TextView mLikesCount = holder.mLikesCount;
        final ImageView imgProfile = holder.imgProfile;
        final ImageView imgDropdown = holder.imgDropdown;
        final ImageView imgLikes = holder.imgLikes;

        mUsername.setText(model.getMessageUser());
        mText.setText(model.getMessageText());
        mTime.setText(DateFormat.format("dd MMM  (h:mm a)", model.getMessageTime()));
        mLikesCount.setText(String.valueOf(model.getMessageLikesCount()));
        if(model.getMessageLikes()!=null){
            if(model.getMessageLikes().containsValue(userId)){
                imgLikes.setImageResource(R.drawable.ic_launcher_foreground);
            }
            else{
                imgLikes.setImageResource(R.drawable.ic_launcher_foreground1);
            }
        }
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(storageReference.child(model.getMessageUserId()))
                .into(imgProfile);

    }

    public class MessageHolder extends RecyclerView.ViewHolder {
        TextView mText;
        TextView mUsername;
        TextView mTime;
        TextView mLikesCount;
        ImageView imgProfile, imgDropdown, imgLikes;
        public MessageHolder(View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.message_text);
            mUsername = itemView.findViewById(R.id.username);
            mTime = itemView.findViewById(R.id.message_time);
            mLikesCount = itemView.findViewById(R.id.message_Likes);
            imgProfile = itemView.findViewById(R.id.imgdp);
            imgLikes = itemView.findViewById(R.id.imgLikes);
            imgDropdown = itemView.findViewById(R.id.imgDropdwon);
        }
    }
}