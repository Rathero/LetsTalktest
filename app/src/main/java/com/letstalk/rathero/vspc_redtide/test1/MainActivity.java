package com.letstalk.rathero.vspc_redtide.test1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CreateTalk.OnFragmentInteractionListener, TalksList.OnFragmentInteractionListener, Settings.OnFragmentInteractionListener,
        TalkView.OnFragmentInteractionListener {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleLoginHelper.SetUpAuth(this);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CreateTalk();
                getSupportActionBar().setTitle("Create Talk");
                SetFragment(fragment);
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = FragmentHelper.GetTalksList();
        SetFragment(fragment);

    }
    public void UpdateUI(FirebaseUser user){

        TextView currentUserEmail = (TextView) findViewById(R.id.currentUserEmail);
        TextView currentUserUsername = (TextView) findViewById(R.id.currentUserUsername);
        ImageView currentUserImage = (ImageView) findViewById(R.id.currentUserImage);
        if(user != null) {
            currentUserEmail.setText(user.getEmail());
            currentUserUsername.setText(user.getDisplayName());
            Picasso.with(this)
                    .load(user.getPhotoUrl())
                    .into(currentUserImage);                        //Your image view object.
        }
        else{
            currentUserEmail.setText("");
            currentUserUsername.setText("");
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        FirebaseUser currentUser = GoogleLoginHelper.mAuth.getCurrentUser();
        UpdateUI(currentUser);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        LinearLayout layout_search = (LinearLayout) findViewById(R.id.layout_search);
        if(layout_search != null) {
            layout_search.setVisibility(View.VISIBLE);
            final EditText inputSearchTalk = (EditText) findViewById(R.id.input_search_talk);
            inputSearchTalk.requestFocus();
            layout_search.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager keyboard = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(inputSearchTalk, 0);
                }
            }, 100);
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_talks) {
            fragment = FragmentHelper.GetTalksList();
            TalksList talksList = (TalksList) fragment;
            talksList.SetCategory("");
            talksList.SetMyTalks(false);
            if (fragment.isVisible()) {
                talksList.GetLastTalks();
            }
            getSupportActionBar().setTitle("Let's talk");
        }
        else if(id == R.id.nav_mytalks){
            fragment = FragmentHelper.GetTalksList();
            TalksList talksList = (TalksList) fragment;
            talksList.SetCategory("");
            talksList.SetMyTalks(true);
            if (fragment.isVisible()) {
                talksList.GetLastTalks();
            }
            getSupportActionBar().setTitle("My talks");
        }
        else if(id == R.id.nav_categories){
            fragment = new CategoriesList();
            getSupportActionBar().setTitle("Categories");
        }
        else if(id == R.id.nav_languages){
            fragment = new LanguagesList();
            getSupportActionBar().setTitle("Languages");
        }
        else if (id == R.id.nav_create_talk) {
            fragment = new CreateTalk();
            getSupportActionBar().setTitle("Create Talk");
        }
        else if(id == R.id.nav_settings){
            fragment = new Settings();
            getSupportActionBar().setTitle("Settings");
        }
        else if (id == R.id.nav_share) {

        }
        SetFragment(fragment);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void SetFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, "gallery");
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
