package com.letstalk.rathero.vspc_redtide.test1;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.letstalk.rathero.vspc_redtide.test1.R;
import com.letstalk.rathero.vspc_redtide.test1.database.ViewedTalks;

import java.util.ArrayList;
import java.util.List;


public class TalkAdapter extends ArrayAdapter<Talk> {

    private Talk talk;
    private Context Context;
    public TalkAdapter(Context context, ArrayList<Talk> talks) {
        super(context, 0, talks);
        this.Context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        talk = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_talk, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.talk_title);
        tvName.setText(talk.Title);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.talk_description);
        tvDescription.setText(talk.Description);
        TextView tvCategory = (TextView) convertView.findViewById(R.id.talk_category);
        tvCategory.setText(talk.Category);
        TextView tvLanguage = (TextView) convertView.findViewById(R.id.talk_language);
        if(!TextUtils.isEmpty(talk.Language)) {
            tvLanguage.setText("Language: " + talk.Language);
        }
        else tvLanguage.setVisibility(View.GONE);

        final TextView tvNewMessages = (TextView) convertView.findViewById(R.id.talk_newmessages);
        final RelativeLayout layoutNewMessages = (RelativeLayout) convertView.findViewById(R.id.layout_newmesages_badge);
        layoutNewMessages.setVisibility(View.GONE);
        TextView tvId = (TextView) convertView.findViewById(R.id.talk_id);
        tvId.setText(talk.Id);


        CardView card = (CardView) convertView.findViewById(R.id.card_view);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new TalkView();

                Bundle args = new Bundle();

                TextView tvId = (TextView) view.findViewById(R.id.talk_id);
                args.putString("id", tvId.getText().toString());
                fragment.setArguments(args);

                SetFragment(fragment, Context);
            }
        });

        List<ViewedTalks> viewedTalks = ViewedTalks.find(ViewedTalks.class, "talk_id = ?", talk.Id);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        if(viewedTalks != null && !viewedTalks.isEmpty()) {
            final ArrayList<Message> newMessages = new ArrayList<Message>();
            myRef.child("talks").child(talk.Id).child("messages").orderByChild("TimeStamp").endAt(viewedTalks.get(0).LastReview).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(dataSnapshot.getValue() != null) {
                        Message message = dataSnapshot.getValue(Message.class);
                        newMessages.add(message);
                        if(newMessages.size() > 0) {
                            tvNewMessages.setText(String.valueOf(newMessages.size()));
                            layoutNewMessages.setVisibility(View.VISIBLE);
                        }
                    }
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return convertView;
    }
    public void SetFragment(Fragment fragment, Context context){
        FragmentTransaction fragmentTransaction = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, "gallery").addToBackStack("talkadapter");
        fragmentTransaction.commitAllowingStateLoss();
    }
}
