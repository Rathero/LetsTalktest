package com.letstalk.rathero.vspc_redtide.test1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.letstalk.rathero.vspc_redtide.test1.database.SubscribedTalk;

import java.util.ArrayList;
import java.util.List;

import android.view.WindowManager;
import android.widget.TextView;


public class TalksList extends Fragment {
    private OnFragmentInteractionListener mListener;
    String category;
    String language;
    boolean mytalks;
    ArrayList<Talk> arrayOfTalks;
    View v;
    public TalksList() {
        // Required empty public constructor
        category = "";
    }

    public void SetCategory(String category){
        this.category = category;
    }
    public void SetLanguage(String language){
        this.language = language;
    }
    public void SetMyTalks(boolean mytalks){
        this.mytalks = mytalks;
    }

    public void SetSearchListener(){
        Button editText = (Button) v.findViewById(R.id.button_search__talk);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetLastTalks();
            }
        });
    }
    public void GetMyTalks(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        List<SubscribedTalk> subscribedTalks = SubscribedTalk.listAll(SubscribedTalk.class);
        arrayOfTalks = new ArrayList<>();
        TalkAdapter adapter = new TalkAdapter(getContext(), arrayOfTalks);
        ListView listView = (ListView) v.findViewById(R.id.listview_talks);
        final TextView textViewNoTalks = (TextView) v.findViewById(R.id.textview_notalks);
        textViewNoTalks.setVisibility(View.VISIBLE);

        listView.setAdapter(adapter);
        for(SubscribedTalk subscribedTalk : subscribedTalks){
            Query query = myRef.child("talks").child(subscribedTalk.TalkId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Talk talk = dataSnapshot.getValue(Talk.class);
                    if(getContext() != null) {
                        arrayOfTalks.add(talk);
                        TalkAdapter adapter = new TalkAdapter(getContext(), arrayOfTalks);
                        ListView listView = (ListView) v.findViewById(R.id.listview_talks);
                        listView.setAdapter(adapter);
                        textViewNoTalks.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }
    public void GetLastTalks(){
        if(!TextUtils.isEmpty(category)){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(category);
        }
        if(!TextUtils.isEmpty(language)){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(language);
        }
        if(mytalks){
            GetMyTalks();
            return;
        }
        final TextView textViewNoTalks = (TextView) v.findViewById(R.id.textview_notalks);
        textViewNoTalks.setVisibility(View.VISIBLE);
        EditText editText = (EditText) v.findViewById(R.id.input_search_talk);
        String search = editText.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        Query query = myRef.child("talks");
        if(!TextUtils.isEmpty(search)){
            query = query.orderByChild("Title").equalTo(search);
        }
        query = query.limitToLast(10);
        arrayOfTalks = new ArrayList<>();
        TalkAdapter adapter = new TalkAdapter(getContext(), arrayOfTalks);
        ListView listView = (ListView) v.findViewById(R.id.listview_talks);
        listView.setAdapter(adapter);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Talk talk = dataSnapshot.getValue(Talk.class);
                boolean validTalk = true;
                if(!TextUtils.isEmpty(category)){
                    validTalk = category.equals(talk.Category);
                }
                if(!TextUtils.isEmpty(language)){
                    validTalk = language.equals(talk.Language);
                }
                if(getContext() != null && validTalk) {
                    arrayOfTalks.add(talk);
                    TalkAdapter adapter = new TalkAdapter(getContext(), arrayOfTalks);
                    ListView listView = (ListView) v.findViewById(R.id.listview_talks);
                    listView.setAdapter(adapter);
                    textViewNoTalks.setVisibility(View.GONE);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("onCreate", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.v = inflater.inflate(R.layout.fragment_talks_list, container, false);
        return this.v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        EditText editText = (EditText) v.findViewById(R.id.input_search_talk);
        editText.getText().clear();
        GetLastTalks();
        SetSearchListener();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
