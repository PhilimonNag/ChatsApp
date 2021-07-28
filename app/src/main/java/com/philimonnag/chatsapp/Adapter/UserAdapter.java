package com.philimonnag.chatsapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philimonnag.chatsapp.Model.User;
import com.philimonnag.chatsapp.R;
import com.philimonnag.chatsapp.databinding.ItemUserBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    ArrayList<User>arrayList;
    FirebaseUser firebaseUser;

    public UserAdapter(Context context, ArrayList<User> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        User user=arrayList.get(position);
        holder.binding.Uname.setText(user.getPersonName());
        Picasso.get().load(user.getPersonPhoto()).into(holder.binding.profilepic);
        LastMessage(user.getPersonId(),holder.binding.Ubio,holder.binding.mTime);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();
                bundle.putString("uName", user.getPersonName());
                bundle.putString("uEmail",user.getPersonEmail());
                bundle.putString("uImg", user.getPersonPhoto());
                bundle.putString("uId", user.getPersonId());
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_chatFragment,bundle);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemUserBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ItemUserBinding.bind(itemView);
        }
    }
    private void LastMessage(String uid, TextView textView, TextView mTv) {
        FirebaseDatabase.getInstance().getReference().child("lastChat").
                child(uid+firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    textView.setText(snapshot.child("lastMsg").getValue(String.class));
                    mTv.setText(snapshot.child("lastMsgtime").getValue(String.class));
                }else {
                    textView.setText("Tap to Chat");
                    mTv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



    }
}
