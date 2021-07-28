package com.philimonnag.chatsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.philimonnag.chatsapp.Adapter.MessageAdapter;
import com.philimonnag.chatsapp.Model.Message;
import com.philimonnag.chatsapp.Model.User;
import com.philimonnag.chatsapp.databinding.FragmentChatBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatFragment extends Fragment {
private FragmentChatBinding binding;
FirebaseUser firebaseUser;
ArrayList<Message>arrayList;
MessageAdapter adapter;
    String SenderRoom,ReceiverRoom,uEmail;
    String receiverUid;
    String senderUid;
    private static  int TOTAL_ITEM=10;
    private int mCurrentPage=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentChatBinding.inflate(inflater,container,false);
        View root=binding.getRoot();
        binding.textUserName.setText(getArguments().getString("uName"));
        Picasso.get().load(getArguments().getString("uImg")).into(binding.chatProfileImg);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        senderUid = firebaseUser.getUid();
        receiverUid=getArguments().getString("uId");
        uEmail=getArguments().getString("uEmail");
        SenderRoom =senderUid+receiverUid;
        ReceiverRoom=receiverUid+senderUid;
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_chatFragment_to_homeFragment);
            }
        });
        FirebaseDatabase.getInstance().getReference().child("user")
                .child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            User user=snapshot.getValue(User.class);
                            assert user != null;
                            uEmail=user.getPersonEmail();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
        binding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msgTxt=binding.message.getText().toString();
                binding.message.setText("");
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                String time = sdf.format(new Date());
                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                String timeStamp=simpleDateFormat.format(new Date());
                Message message=new Message(msgTxt,uEmail,timeStamp);
                HashMap<String , Object> last=new HashMap<>();
                last.put("lastMsg",message.getMessage());
                last.put("lastMsgtime",time);
                FirebaseDatabase.getInstance().getReference().child("chats")
                        .child(SenderRoom)
                        .child("messages")
                        .push()
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseDatabase.getInstance().getReference().child("chats")
                                .child(ReceiverRoom)
                                .child("messages")
                                .push()
                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
            }
        });
         arrayList= new ArrayList<>();
        adapter = new MessageAdapter(getContext(),arrayList,uEmail);
        binding.messageRV.setHasFixedSize(true);
        binding.messageRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false ));

        binding.messageRV.setAdapter(adapter);
        loadMessage();
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage++;
                arrayList.clear();
                loadMessage();
            }
        });
        return root;
    }

    private void loadChats() {
        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(SenderRoom).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    Toast.makeText(getContext(), (int) snapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        Query query=FirebaseDatabase.getInstance().getReference().child("chats")
                .child(SenderRoom).child("messages")
                .limitToLast(10);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                arrayList.clear();
                if(snapshot.exists()){
                    for(DataSnapshot me:snapshot.getChildren()){
                        Message messages=me.getValue(Message.class);
                        arrayList.add(messages);
                        binding.messageRV.scrollToPosition(arrayList.lastIndexOf(messages));
                        binding.swipeRefresh.setRefreshing(false);
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
   private void loadMessage(){
       Query query=FirebaseDatabase.getInstance().getReference().child("chats")
               .child(SenderRoom).child("messages")
               .limitToLast(mCurrentPage*TOTAL_ITEM);
       query.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
               Message messages=snapshot.getValue(Message.class);
               arrayList.add(messages);
              // binding.messageRV.scrollToPosition(arrayList.lastIndexOf(messages));
               binding.messageRV.scrollToPosition(arrayList.size()-1);
               binding.swipeRefresh.setRefreshing(false);
           }

           @Override
           public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

           }

           @Override
           public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

           }

           @Override
           public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

           }

           @Override
           public void onCancelled(@NonNull @NotNull DatabaseError error) {

           }
       });
    }
}