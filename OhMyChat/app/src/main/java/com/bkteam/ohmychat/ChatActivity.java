package com.bkteam.ohmychat;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String messageReceiverID, messageReceiverName, messageReceiverImage;
    private TextView userName, userLastSeen;
    private CircleImageView userImage;
    private Toolbar chatToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageReceiverID = getIntent().getExtras().get("visitUserId").toString();
        messageReceiverName = getIntent().getExtras().get("visitUserName").toString();
        messageReceiverImage = getIntent().getExtras().get("visitUserImage").toString();

        Toast.makeText(ChatActivity.this, messageReceiverID, Toast.LENGTH_LONG).show();
        Toast.makeText(ChatActivity.this, messageReceiverName, Toast.LENGTH_LONG).show();

        InitializeControllers();

        Picasso.get().load(messageReceiverImage).placeholder(R.drawable.profile_image).into(userImage);
    }

    private void InitializeControllers() {
        chatToolBar = (Toolbar)findViewById(R.id.chat_toolbar);

        setSupportActionBar(chatToolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.custom_chat_bar, null);

        actionBar.setCustomView(actionBarView);

        userImage = (CircleImageView)findViewById(R.id.custom_profile_image);
        userName = (TextView)findViewById(R.id.custom_profile_name);
        userLastSeen = (TextView)findViewById(R.id.custom_user_last_seen);

    }
}
