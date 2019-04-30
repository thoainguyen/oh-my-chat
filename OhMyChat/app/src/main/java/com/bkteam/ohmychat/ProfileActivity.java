package com.bkteam.ohmychat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private String receiverUserId, currentState, senderUserId;
    private CircleImageView userProfileImage;
    private TextView userProfileName, userProfileStatus;
    private Button sendMessageRequestButton, declineMessageRequestButton;
    private DatabaseReference userRef, chatRequestRef, contactsRef, notificationRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        chatRequestRef = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        notificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications");

        mAuth = FirebaseAuth.getInstance();
        senderUserId = mAuth.getCurrentUser().getUid();

        receiverUserId = getIntent().getExtras().get("visitUserId").toString();
        // Toast.makeText(this, "User ID : " + receiverUserId, Toast.LENGTH_SHORT).show();

        userProfileImage = (CircleImageView)findViewById(R.id.visit_profile_image);
        userProfileName = (TextView)findViewById(R.id.visit_user_name);
        userProfileStatus = (TextView)findViewById(R.id.visit_profile_status);
        sendMessageRequestButton = (Button)findViewById(R.id.send_message_request_button);
        declineMessageRequestButton = (Button)findViewById(R.id.decline_message_request_button);
        currentState = "new";

        RetrieveUserInfo();
    }

    private void RetrieveUserInfo() {
        userRef.child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("image"))){
                    String userImage = dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(userProfileImage);
                }

                String userName = dataSnapshot.child("name").getValue().toString();
                String userStatus = dataSnapshot.child("status").getValue().toString();

                userProfileName.setText(userName);
                userProfileStatus.setText(userStatus);
                ManageChatRequests();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void ManageChatRequests() {

        chatRequestRef.child(senderUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(receiverUserId)){
                    String requestType = dataSnapshot.child(receiverUserId).child("requestType").getValue().toString();
                    if(requestType.equals("sent")){
                        currentState = "requestSend";
                        sendMessageRequestButton.setText("Cancel Chat Request");
                    }
                    else if(requestType.equals("received")){
                        currentState = "requestReceived";
                        sendMessageRequestButton.setText("Accept Chat Request");
                        declineMessageRequestButton.setVisibility(View.VISIBLE);
                        declineMessageRequestButton.setEnabled(true);
                        declineMessageRequestButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CancelChatRequest();
                            }
                        });
                    }

                }
                else {
                    contactsRef.child(receiverUserId)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(receiverUserId)){
                                        currentState = "friends";
                                        sendMessageRequestButton.setText("Remove this Contact");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(!senderUserId.equals(receiverUserId)){
            sendMessageRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessageRequestButton.setEnabled(false);
                    if(currentState.equals("new")){
                        SendChatRequest();
                    }
                    if(currentState.equals("requestSend")){
                        CancelChatRequest();
                    }
                    if(currentState.equals("requestReceived")){
                        AcceptChatRequest();
                    }
                    if(currentState.equals("friends")){
                        RemoveSpecificContact();
                    }
                }
            });
        }
        else{
            sendMessageRequestButton.setVisibility(View.INVISIBLE);
            declineMessageRequestButton.setVisibility(View.INVISIBLE);
        }
    }

    private void RemoveSpecificContact() {
        contactsRef.child(senderUserId).child(receiverUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            contactsRef.child(receiverUserId).child(senderUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                sendMessageRequestButton.setEnabled(true);
                                                currentState = "new";
                                                sendMessageRequestButton.setText("Send Message");
                                                declineMessageRequestButton.setVisibility(View.INVISIBLE);
                                                declineMessageRequestButton.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void AcceptChatRequest() {
        contactsRef.child(senderUserId).child(receiverUserId)
                .child("Contacts").setValue("Saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            contactsRef.child(receiverUserId).child(senderUserId)
                                    .setValue("Saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                chatRequestRef.child(senderUserId).child(receiverUserId)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    chatRequestRef.child(receiverUserId).child(senderUserId)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    sendMessageRequestButton.setEnabled(true);
                                                                                    currentState = "friends";
                                                                                    sendMessageRequestButton.setText("Remove this Contacts");
                                                                                    declineMessageRequestButton.setVisibility(View.INVISIBLE);
                                                                                    declineMessageRequestButton.setEnabled(false);
                                                                                }
                                                                            });
                                                                }

                                                            }
                                                        });

                                            }
                                        }
                                    });

                        }
                    }
                });
    }

    private void CancelChatRequest() {
        chatRequestRef.child(senderUserId).child(receiverUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            chatRequestRef.child(receiverUserId).child(senderUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                sendMessageRequestButton.setEnabled(true);
                                                currentState = "new";
                                                sendMessageRequestButton.setText("Send Message");
                                                declineMessageRequestButton.setVisibility(View.INVISIBLE);
                                                declineMessageRequestButton.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void SendChatRequest() {
        chatRequestRef.child(senderUserId).child(receiverUserId)
                .child("requestType").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            chatRequestRef.child(receiverUserId).child(senderUserId)
                                    .child("requestType").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                HashMap<String,String> chatNotificationMap = new HashMap<>();
                                                chatNotificationMap.put("from",senderUserId);
                                                chatNotificationMap.put("type","request");
                                                notificationRef.child(receiverUserId).push()
                                                        .setValue(chatNotificationMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    sendMessageRequestButton.setEnabled(true);
                                                                    currentState = "requestSent";
                                                                    sendMessageRequestButton.setText("Cancel Chat Request");
                                                                }
                                                            }
                                                        });

                                                sendMessageRequestButton.setEnabled(true);
                                                currentState = "requestSend";
                                                sendMessageRequestButton.setText("Cancel Chat Request");
                                                declineMessageRequestButton.setVisibility(View.INVISIBLE);
                                                declineMessageRequestButton.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
