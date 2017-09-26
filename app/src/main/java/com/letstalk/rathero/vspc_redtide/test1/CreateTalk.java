package com.letstalk.rathero.vspc_redtide.test1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.letstalk.rathero.vspc_redtide.test1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CreateTalk extends Fragment implements TalkView.OnFragmentInteractionListener {

    private Context context;
    private OnFragmentInteractionListener mListener;

    public CreateTalk() {
    }

    public void CreateNewTalk(View v){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        TextView inputTitleTalk = (TextView) v.findViewById(R.id.input_titleTalk);
        TextView inputDescriptionTalk = (TextView) v.findViewById(R.id.input_descriptionTalk);
        Spinner spinnerCategory = (Spinner) v.findViewById(R.id.selector_categoryTalk);
        Spinner spinnerLanguage = (Spinner) v.findViewById(R.id.selector_languageTalk);
        String title = inputTitleTalk.getText().toString();
        if(TextUtils.isEmpty(title)){
            Snackbar.make(v, "Please, write a title.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else {
            String newTalkId = myRef.child("talks").push().getKey();
            Talk talk = new Talk(title, inputDescriptionTalk.getText().toString(), spinnerCategory.getSelectedItem().toString(), spinnerLanguage.getSelectedItem().toString(), newTalkId);


            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(newTalkId, talk);

            myRef.child("talks").updateChildren(childUpdates);
            Snackbar.make(v, "Talk created succesfully", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            inputDescriptionTalk.setText("");
            inputTitleTalk.setText("");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_create_talk, container, false);

        Button createTalk = (Button) view.findViewById(R.id.button_createTalk);
        createTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewTalk(view);
            }
        });

        final Spinner categoriesSelector = (Spinner) view.findViewById(R.id.selector_categoryTalk);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        final ArrayList<String> arrayOfCategories = new ArrayList<String>();
        myRef.child("categories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue() != null) {
                    Category category = dataSnapshot.getValue(Category.class);
                    arrayOfCategories.add(category.Name);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arrayOfCategories);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    categoriesSelector.setAdapter(spinnerArrayAdapter);
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
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
