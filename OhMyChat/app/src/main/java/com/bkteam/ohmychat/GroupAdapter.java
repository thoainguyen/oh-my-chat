package com.bkteam.ohmychat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAdapter extends ArrayAdapter<GroupMessage> {
    private ArrayList<GroupMessage> dataSet;
    Context mContext;

    private static class ViewHolder{
        CircleImageView receiverAvt;
        TextView    receiverName;
        TextView    receiverMess;
        TextView    receiverTime;
        TextView    senderMess;
        TextView    senderTime;
    }

    public GroupAdapter(ArrayList<GroupMessage> data, Context context){
        super(context,R.layout.item_message_group,data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        GroupMessage groupMess = getItem(position);
        ViewHolder viewHolder;
        final View result;

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_message_group,parent,false);
            viewHolder.receiverAvt = (CircleImageView)convertView.findViewById(R.id.image_message_profile);
            viewHolder.receiverName = (TextView)convertView.findViewById(R.id.text_message_name);
            viewHolder.receiverMess = (TextView)convertView.findViewById(R.id.text_message_receiver);
            viewHolder.receiverTime = (TextView)convertView.findViewById(R.id.text_message_time_receiver);
            viewHolder.senderMess = (TextView)convertView.findViewById(R.id.text_message_send);
            viewHolder.senderTime = (TextView)convertView.findViewById(R.id.text_message_time_send);
            result=convertView;
                convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.receiverAvt.setVisibility(View.INVISIBLE);
        viewHolder.receiverName.setVisibility(View.INVISIBLE);
        viewHolder.receiverMess.setVisibility(View.INVISIBLE);
        viewHolder.receiverTime.setVisibility(View.INVISIBLE);
        viewHolder.senderMess.setVisibility(View.INVISIBLE);
        viewHolder.senderTime.setVisibility(View.INVISIBLE);

        if (groupMess.getSeMsg().equals("")){
            viewHolder.receiverName.setVisibility(View.VISIBLE);
            viewHolder.receiverAvt.setVisibility(View.VISIBLE);
            viewHolder.receiverMess.setVisibility(View.VISIBLE);
            viewHolder.receiverTime.setVisibility(View.VISIBLE);
            viewHolder.receiverName.setText(groupMess.getReName());
            viewHolder.receiverMess.setText(groupMess.getReMsg());
            viewHolder.receiverTime.setText(groupMess.getReTime() + " "+groupMess.getReDate().substring(0,groupMess.getReDate().length()-6));
        }
        else {
            viewHolder.senderMess.setVisibility(View.VISIBLE);
            viewHolder.senderTime.setVisibility(View.VISIBLE);
            viewHolder.senderMess.setText(groupMess.getSeMsg());
            viewHolder.senderTime.setText(groupMess.getSeTime()+" "+groupMess.getSeDate().substring(0,groupMess.getSeDate().length()-6));
        }

        return convertView;
    }
}
