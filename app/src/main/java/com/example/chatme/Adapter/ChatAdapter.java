package com.example.chatme.Adapter;
//it will have 2 view holder classs as there are 2 layouts one bubble for sender and
//one for reciever

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatme.R;
import com.example.chatme.messagesModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{
    ArrayList<messagesModel> messageModels;
    Context context;
    int SENDER_VIEW_TYPE=1;
    int RECIEVER_VIEW_TYPE=2;

    public ChatAdapter(ArrayList<messagesModel> list, Context context) {
        this.messageModels = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==SENDER_VIEW_TYPE)
       {

           View view= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
           return  new senderViewHolder(view);
       }
       else
       {
           View view= LayoutInflater.from(context).inflate(R.layout.sample_reciever,parent,false);
           return  new recieverViewHolder(view);

       }

    }
//this function is overridden to decide which msgd too get into which chat bubble
    @Override
    public int getItemViewType(int position) {
        if(messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            Log.d("type",Integer.toString(SENDER_VIEW_TYPE));
            return SENDER_VIEW_TYPE;
        }
        else {
            Log.d("type",Integer.toString(RECIEVER_VIEW_TYPE));

            return RECIEVER_VIEW_TYPE;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
messagesModel model= messageModels.get(position);
        Toast.makeText(context, "this toast is imp", Toast.LENGTH_SHORT).show();
        Log.d("imp","setTextSender");
if(holder.getClass()==senderViewHolder.class)
{
    Log.d("imp","setTextSender");
    ((senderViewHolder)holder).sendermsg.setText(model.getMessages());
  //  ((senderViewHolder)holder).senderTime.setText(model.getTimestamp().toString());

}
else
{

    Log.d("imp","setTextReciever");
    ((recieverViewHolder)holder).recievermsg.setText(model.getMessages());
   // ((recieverViewHolder)holder).recieverTime.setText(model.getTimestamp().toString());
}
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class recieverViewHolder extends RecyclerView.ViewHolder{
        TextView recievermsg,recieverTime;

        public recieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recievermsg=itemView.findViewById(R.id.messageReciever);
            recieverTime=itemView.findViewById(R.id.timestampRecieved);
        }
    }
    public class senderViewHolder extends RecyclerView.ViewHolder{
        TextView sendermsg,senderTime;

        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            sendermsg=itemView.findViewById(R.id.messagesender);
            senderTime=itemView.findViewById(R.id.timestampSend);
        }
    }
}
