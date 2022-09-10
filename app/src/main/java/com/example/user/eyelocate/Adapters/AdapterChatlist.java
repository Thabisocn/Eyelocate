package com.example.user.eyelocate.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.eyelocate.ChatActivity;
import com.example.user.eyelocate.Models.ModelUsers;
import com.eyelocate.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdapterChatlist extends RecyclerView.Adapter<AdapterChatlist.MyHolder> {


    Context context;
    List<ModelUsers> usersList;
    private HashMap<String, String> lastMessageMap;


    public AdapterChatlist(Context context, List<ModelUsers> usersList) {
        this.context = context;
        this.usersList = usersList;
        lastMessageMap = new HashMap<>();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_chatlist, viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {


        //get data
        final String hisUid = usersList.get(i).getUid();
        String userImage = usersList.get(i).getImage();
        String userName = usersList.get(i).getName();
        String lastMessage = lastMessageMap.get(hisUid);

        //SET DATA

        myHolder.nameTv.setText(userName);
        if (lastMessage == null || lastMessage.equals("default")){
            myHolder.lastMessageTv.setVisibility(View.GONE);
        }else{
            myHolder.lastMessageTv.setVisibility(View.VISIBLE);
            myHolder.lastMessageTv.setText(lastMessage);
        }

        try {

            Picasso.get().load(userImage).placeholder(R.drawable.default_image).into(myHolder.profileIv);
        }catch (Exception e){

            Picasso.get().load(R.drawable.default_image).into(myHolder.profileIv);
        }

        //set online status of the other user on the chatlist

        if (usersList.get(i).getOnlineStatus().equals("online")){
            //online
           myHolder.online_statusIv.setImageResource(R.drawable.circle_online);
        }
        else {
            //offline
            myHolder.online_statusIv.setImageResource(R.drawable.circle_offline);
        }

        //handle click of user in chatlist

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start the chat activity

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("hisUid", hisUid);
                context.startActivity(intent);
            }
        });

    }

    public void setLastMessageMap(String userId, String lastMessage){
        lastMessageMap.put(userId, lastMessage);

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView profileIv, online_statusIv;
        TextView nameTv, lastMessageTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            profileIv = itemView.findViewById(R.id.profileIv);
            online_statusIv = itemView.findViewById(R.id.online_statusIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            lastMessageTv = itemView.findViewById(R.id.lastMessageTv);
        }
    }
}
