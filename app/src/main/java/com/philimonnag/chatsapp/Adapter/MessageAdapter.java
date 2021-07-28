package com.philimonnag.chatsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philimonnag.chatsapp.Model.Message;
import com.philimonnag.chatsapp.Model.User;
import com.philimonnag.chatsapp.R;
import com.philimonnag.chatsapp.databinding.ItemMessageBinding;
import com.philimonnag.chatsapp.databinding.ItemReceivedBinding;
import com.philimonnag.chatsapp.databinding.ItemSendBinding;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Message>messagesArrayList;
    private FirebaseUser firebaseUser;
    final int RECEIVE_MSG=1;
    final int SENDER_MSG=2;
    String email;

    public MessageAdapter(Context context, ArrayList<Message> messagesArrayList, String email) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
        this.email = email;
    }

    @Override
    public int getItemViewType(int position) {
        Message model= messagesArrayList.get(position);
        if(model.getSenderEmail().equals(email)){
            return RECEIVE_MSG;
        }else {
            return SENDER_MSG;
        }
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(viewType==SENDER_MSG){
            View v = LayoutInflater.from(context).inflate(R.layout.item_send,parent,false);
            return new Sender(v);
        }else {
            View v= LayoutInflater.from(context).inflate(R.layout.item_received,parent,false);
            return new Receiver(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        Message messages = messagesArrayList.get(position);
        if(holder.getClass()==Sender.class){
            Sender viewHolder=(Sender)holder;
            viewHolder.binding.sentMsg.setText(messages.getMessage());
            viewHolder.binding.sendTime.setText(messages.getTimeStamp());
            viewHolder.binding.sentEmail.setText(messages.getSenderEmail());
        }else {
            Receiver viewHolder=(Receiver)holder;
            viewHolder.binding.receivedMsg.setText(messages.getMessage());
            viewHolder.binding.receivedTime.setText(messages.getTimeStamp());
            viewHolder.binding.receiveEmail.setText(messages.getSenderEmail());
        }

    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }
    public class Sender extends RecyclerView.ViewHolder{
        ItemSendBinding binding;
        public Sender(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = ItemSendBinding.bind(itemView);
        }
    }
    public class Receiver extends RecyclerView.ViewHolder{
        ItemReceivedBinding binding;
        public Receiver(@NonNull @NotNull View itemView) {
            super(itemView);
            binding= ItemReceivedBinding.bind(itemView);
        }
    }
}
