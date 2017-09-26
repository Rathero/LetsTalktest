package com.letstalk.rathero.vspc_redtide.test1;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class MessageAdapter  extends ArrayAdapter<Message> {
    private Message message;
    private android.content.Context Context;

    public MessageAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
        this.Context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        message = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
        }
        TextView tvText = (TextView) convertView.findViewById(R.id.message_text);
        tvText.setText(message.Text);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.message_username);
        tvUsername.setText(message.Username);

        TextView tvTime = (TextView) convertView.findViewById(R.id.message_time);
        tvTime.setText(getDateCurrentTimeZone(message.TimeStamp));

        FirebaseUser currentUser = GoogleLoginHelper.mAuth.getCurrentUser();
        CardView card = (CardView) convertView.findViewById(R.id.card_view);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams();
        if(currentUser != null && message.Username.equals(currentUser.getDisplayName())) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }
        else{
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        }
        card.setLayoutParams(params);
        return convertView;
    }
    public  String getDateCurrentTimeZone(long timestamp) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date resultdate = new Date(-timestamp);
            return sdf.format(resultdate);
        }catch (Exception e) {
        }
        return "";
    }
}
