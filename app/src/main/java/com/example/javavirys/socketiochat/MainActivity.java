package com.example.javavirys.socketiochat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.javavirys.socketiochat.list.models.Message;
import com.example.javavirys.socketiochat.list.MessagesAdapter;
import com.example.javavirys.socketiochat.list.models.MessagesList;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_ANSWER = 1001;
    public static final String CHAT_NAME = "chat";

    @BindView(R.id.main_list) ListView listView;
    @BindView(R.id.main_edit) EditText editMsg;
    @BindView(R.id.main_send) Button sendMsg;

    private MessagesAdapter adapter;

    private ValueEventListener valueListener = null;

    private List<Message> myList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUi();
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),REQUEST_ANSWER);
        else
            loadChatData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(valueListener != null) {
            FirebaseDatabase.getInstance().getReference(CHAT_NAME).removeEventListener(valueListener);
            valueListener = null;
        }
    }

    void initUi(){
        listView.setAdapter(adapter = new MessagesAdapter(this));
        sendMsg.setOnClickListener(v->{
            String msg = editMsg.getText().toString();
            if(msg.trim().isEmpty()) {
                Snackbar.make(v, getString(R.string.enter_msg), Snackbar.LENGTH_LONG).show();
                return;
            }
            Message message = new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                    msg,System.currentTimeMillis(), Gravity.LEFT);
            myList.add(message);
            syncMessages();
            editMsg.getText().clear();
        });
    }

    void loadChatData(){
        if(valueListener == null) {
            FirebaseDatabase.getInstance().getReference(CHAT_NAME).addValueEventListener(valueListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    MessagesList messages = dataSnapshot.getValue(MessagesList.class);
                    if (messages != null) {

                        for(Message message : messages.getList()){
                            if(message == null)
                                continue;
                            message.setGravity(
                                    message.getName().equals(
                                            FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                                            && message.getMail().equals(
                                            FirebaseAuth.getInstance().getCurrentUser().getEmail())
                                            ? Gravity.LEFT : Gravity.RIGHT);
                            myList.add(message);
                        }
                        syncMessages();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }
    }

    void syncMessages(){
        synchronized (this) {
            Set<Message> newList = new TreeSet<>((msg1, msg2) -> {
                if (msg1.getTime() == msg2.getTime())
                    return 0;
                else if (msg1.getTime() > msg2.getTime())
                    return 1;

                return -1;
            });
            newList.addAll(myList);

            if (equalsCollections(adapter.getArray(), newList)) {
                return;
            }

            adapter.clear();
            adapter.addAll(newList);
            listView.smoothScrollToPosition(adapter.getCount() - 1);

            MessagesList messages = new MessagesList();
            messages.getList().addAll(newList);
            FirebaseDatabase.getInstance().getReference(CHAT_NAME).setValue(messages);

            myList.clear();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ANSWER && resultCode == RESULT_OK)
            loadChatData();
        else
            finish();
    }

    private boolean equalsCollections(List<Message> list, Set<Message> set){
        if(list.size() != set.size())
            return false;
        for(Message message1 : set){
            boolean found = false;
            for(int i = 0; i < list.size(); i++)
            {
                Message message2 = list.get(i);
                if(message1.getName().equals(message2.getName())
                        && message1.getMsg().equals(message2.getMsg())
                        && message1.getTime() == message2.getTime()){
                    found = true;
                    break;
                }
            }
            if(!found){
                return false;
            }

        }
        return true;
    }

    static void log(String msg){
        Log.d("MainActivity", msg);
    }
}
