package com.bkteam.ohmychat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

    private String receiverUserId, senderUserId;
    private CircleImageView userProfileImage;
    private TextView userProfileName, userProfileStatus;
    private Button sendMessageRequestButton;
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


        userProfileImage = (CircleImageView) findViewById(R.id.visit_profile_image);
        userProfileName = (TextView) findViewById(R.id.visit_user_name);
        userProfileStatus = (TextView) findViewById(R.id.visit_profile_status);
        sendMessageRequestButton = (Button) findViewById(R.id.send_message_request_button);
        sendMessageRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendChatRequest();
            }
        });

        RetrieveUserInfo();
    }

    private void RetrieveUserInfo() {

        if(senderUserId.equals(receiverUserId)){
            sendMessageRequestButton.setEnabled(false);
            sendMessageRequestButton.setVisibility(View.INVISIBLE);
        }

        userRef.child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image"))) {
                    String userImage = dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(userProfileImage);
                }

                String userName = dataSnapshot.child("name").getValue().toString();
                String userStatus = dataSnapshot.child("status").getValue().toString();

                userProfileName.setText(userName);
                userProfileStatus.setText(userStatus);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    private void SendChatRequest() {
        if(sendMessageRequestButton.getText().equals("Cancel Invited")){
            chatRequestRef.child(senderUserId).child(receiverUserId)
                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        chatRequestRef.child(receiverUserId).child(senderUserId)
                                .removeValue();
                    }
                }
            });
            sendMessageRequestButton.setText("Add Friend");
            return;
        }
        chatRequestRef.child(senderUserId).child(receiverUserId)
                .child("requestType").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            chatRequestRef.child(receiverUserId).child(senderUserId)
                                    .child("requestType").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                HashMap<String, String> chatNotificationMap = new HashMap<>();
                                                chatNotificationMap.put("from", senderUserId);
                                                chatNotificationMap.put("type", "request");
                                                notificationRef.child(receiverUserId).push()
                                                        .setValue(chatNotificationMap);
                                                sendMessageRequestButton.setText("Cancel Invited");
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
