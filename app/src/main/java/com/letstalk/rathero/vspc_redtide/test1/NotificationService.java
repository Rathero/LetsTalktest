package com.letstalk.rathero.vspc_redtide.test1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.letstalk.rathero.vspc_redtide.test1.database.SubscribedTalk;

import java.util.List;


public class NotificationService  extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId){

            List<SubscribedTalk> subscribedTalks = SubscribedTalk.listAll(SubscribedTalk.class);
            for(SubscribedTalk talk : subscribedTalks){
                SubscribeToTalk(talk.TalkId, this);
            }

            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
        public static void SubscribeToTalk(String TalkID, final Context context){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            final Long tsLong = System.currentTimeMillis();
            final DatabaseReference myRef = database.getReference();
            myRef.child("talks").child(TalkID).child("messages").orderByChild("TimeStamp").limitToFirst(15).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(dataSnapshot.getValue() != null) {
                        Message message = dataSnapshot.getValue(Message.class);
                        if(Long.compare(Long.valueOf("-" + tsLong.toString()), message.TimeStamp) > 0) {
                            //if(!currentUser.getDisplayName().equals(message.Username)) {
                                NotificationHelper.ShowNotification(context, message);
                            //}
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
        @Override
        public IBinder onBind(Intent intent){
            return null;
        }
}
