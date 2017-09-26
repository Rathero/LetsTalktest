package com.letstalk.rathero.vspc_redtide.test1;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.letstalk.rathero.vspc_redtide.test1.database.SubscribedTalk;
import com.letstalk.rathero.vspc_redtide.test1.database.ViewedTalks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class TalkView extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Context Context;
    FloatingActionButton fab;
    private String talkId;
    private ArrayList<Message> arrayOfMessages;
    private View v;

    public TalkView() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.fragment_talk_view, container, false);

        Bundle args = getArguments();
        talkId = args.getString("id", "");
        SetTalkViewed();
        fab = (FloatingActionButton) (((AppCompatActivity)getActivity()).findViewById(R.id.fab));
        fab.animate().translationY(300).setInterpolator(new LinearInterpolator()).start();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        myRef.child("talks").child(talkId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Talk talk = dataSnapshot.getValue(Talk.class);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(talk.Title);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        arrayOfMessages = new ArrayList<>();
        myRef.child("talks").child(talkId).child("messages").orderByChild("TimeStamp").limitToFirst(20).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue() != null) {
                        Message message = dataSnapshot.getValue(Message.class);

                        if(arrayOfMessages.isEmpty() || (!arrayOfMessages.isEmpty() && Long.compare(arrayOfMessages.get(0).TimeStamp, message.TimeStamp) < 0)) {
                            arrayOfMessages.add(0, message);
                        }
                        else arrayOfMessages.add(message);
                        if (getContext() != null) {
                            MessageAdapter adapter = new MessageAdapter(getContext(), arrayOfMessages);
                            ListView listView = (ListView) v.findViewById(R.id.listview_messages);
                            listView.setAdapter(adapter);
                            listView.setSelection(listView.getCount() - 1);
                            //NotificationHelper.ShowNotification(Context, message);
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

        Button button =  (Button) v.findViewById(R.id.button_send_message);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewMessage(v);
            }
        });


        ListView listView = (ListView) v.findViewById(R.id.listview_messages);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(mLastFirstVisibleItem<firstVisibleItem){/*Scroll bot*/     }
                if(mLastFirstVisibleItem>firstVisibleItem)
                {
                    if(firstVisibleItem == 0){
                        final Message lastMessage = arrayOfMessages.get(0);
                        myRef.child("talks").child(talkId).child("messages").orderByChild("TimeStamp").startAt(lastMessage.TimeStamp).limitToFirst(20).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if(dataSnapshot.getValue() != null) {
                                    Message message = dataSnapshot.getValue(Message.class);
                                    if(!Objects.equals(arrayOfMessages.get(0).TimeStamp, message.TimeStamp)) {
                                        arrayOfMessages.add(0, message);
                                        if (getContext() != null) {
                                            MessageAdapter adapter = new MessageAdapter(getContext(), arrayOfMessages);
                                            ListView listView = (ListView) v.findViewById(R.id.listview_messages);
                                            listView.setAdapter(adapter);
                                        }
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
                }
                mLastFirstVisibleItem=firstVisibleItem;

            }
        });
        return v;
    }

    public void CreateNewMessage(View v){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        TextView inputMessage = (TextView) v.findViewById(R.id.input_message);

        DatabaseReference child = myRef.child("talks").child(talkId).child("messages");
        String messageText = inputMessage.getText().toString();
        if(!TextUtils.isEmpty(messageText)) {
            String newMessageId = child.push().getKey();
            Message message = new Message();
            message.Text = messageText;
            message.TalkId = talkId;

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            message.Username = currentUser.getDisplayName();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(newMessageId, message);

            child.updateChildren(childUpdates);
            inputMessage.setText("");
        }
        SubscribeToTalk();
        SetTalkViewed();
    }
    private void SubscribeToTalk(){
        boolean talkSaved = false;
        SubscribedTalk.deleteAll(SubscribedTalk.class);
        List<SubscribedTalk> subscribedTalks = SubscribedTalk.listAll(SubscribedTalk.class);
        for(SubscribedTalk talk : subscribedTalks) {
            if(talk.TalkId.equals(talkId)) talkSaved = true;
        }
        if(!talkSaved)
        {
            SubscribedTalk subscribedTalk = new SubscribedTalk(talkId);
            subscribedTalk.save();
        }
        NotificationService.SubscribeToTalk(talkId, Context);
    }

    private void SetTalkViewed(){
        List<ViewedTalks> viewedTalks = ViewedTalks.find(ViewedTalks.class, "talk_id = ?", talkId);
        if(viewedTalks != null && viewedTalks.isEmpty()){
            ViewedTalks viewedTalk = new ViewedTalks(talkId);
            viewedTalk.save();
        }
        else{
            ViewedTalks viewedTalk = viewedTalks.get(0);
            viewedTalk.SetReviewed();
            viewedTalk.save();
        }
    }
    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) Context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Context = context;
        if (!isMyServiceRunning()){
            Intent serviceIntent = new Intent(context, NotificationService.class);
            context.startService(serviceIntent);
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Let's Talk");
        fab.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
