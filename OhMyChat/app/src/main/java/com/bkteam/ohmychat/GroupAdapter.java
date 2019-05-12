package com.bkteam.ohmychat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAdapter extends ArrayAdapter<GroupMess> {
    private ArrayList<GroupMess> dataSet;
    Context mContext;

    private static class ViewHolder{
        CircleImageView receiverAvt;
        TextView    receiverMess;
        TextView    receiverTime;
        TextView    senderMess;
        TextView    senderTime;
    }

    public GroupAdapter(ArrayList<GroupMess> data, Context context){
        super(context,R.layout.item_message_group,data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        GroupMess groupMess = getItem(position);
        ViewHolder viewHolder;
        final View result;

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_message_group,parent,false);
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
        viewHolder.receiverMess.setText(groupMess.getRe_mess());
        viewHolder.receiverTime.setText(groupMess.getRe_date()+ groupMess.getRe_time());
        viewHolder.senderMess.setText(groupMess.getSe_mess());
        viewHolder.senderTime.setText(groupMess.getSe_date()+groupMess.getSe_time());
        return convertView;
    }
}
