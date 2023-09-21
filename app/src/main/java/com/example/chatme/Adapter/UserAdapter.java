package com.example.chatme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatme.R;
import com.example.chatme.UserModel;
import com.example.chatme.chatscreen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends  RecyclerView.Adapter<UserAdapter.ViewHolder>
{
ArrayList<UserModel> list;
Context context;

    public UserAdapter(ArrayList<UserModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.chatcard,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
UserModel userModel=list.get((position));
        Picasso.get().load(userModel.getProfile_pic()).placeholder(R.drawable.chat).into(holder.imageView);
        holder.userName.setText(userModel.getUserName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, chatscreen.class);

                intent.putExtra("userId",userModel.getUserId());
                intent.putExtra("userName",userModel.getUserName());
                intent.putExtra("profilePic",userModel.getProfile_pic());
//              intent.putExtra("userId",userModel.getLastMessage());
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
{
ImageView imageView;
TextView userName,lastMessage;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.userImg);
        userName=itemView.findViewById(R.id.userName);
        lastMessage=itemView.findViewById(R.id.lastmessage);
    }
}
}
