package com.letstalk.rathero.vspc_redtide.test1;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;



public class LanguageAdapter extends ArrayAdapter<Language> {
    private Language language;
    private android.content.Context Context;

    public LanguageAdapter(android.content.Context context, ArrayList<Language> categories) {
        super(context, 0, categories);
        this.Context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        language = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_language, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.language_name);
        tvName.setText(language.Name);

        ImageView currentCategoryImage = (ImageView) convertView.findViewById(R.id.language_image);
        currentCategoryImage.setImageResource(Context.getResources().getIdentifier(language.Name.replace("ñ", "n").toLowerCase(),"drawable",Context.getPackageName()));


        CardView card = (CardView) convertView.findViewById(R.id.language_cardview);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TalksList fragment = FragmentHelper.GetTalksList();
                TextView tvLanguage = (TextView) view.findViewById(R.id.language_name);
                fragment.SetCategory("");
                fragment.SetLanguage(tvLanguage.getText().toString());
                fragment.SetMyTalks(false);

                SetFragment(fragment, Context);
            }
        });

        return convertView;
    }
    public void SetFragment(Fragment fragment, Context context){
        FragmentTransaction fragmentTransaction = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, "gallery").addToBackStack("categoryadapter");
        fragmentTransaction.commitAllowingStateLoss();
    }
}
