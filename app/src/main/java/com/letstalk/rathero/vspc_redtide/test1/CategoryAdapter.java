package com.letstalk.rathero.vspc_redtide.test1;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CategoryAdapter extends ArrayAdapter<Category> {
    private Category category;
    private android.content.Context Context;

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        super(context, 0, categories);
        this.Context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        category = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.category_name);
        tvName.setText(category.Name);

        TextView tvDescription = (TextView) convertView.findViewById(R.id.category_description);
        tvDescription.setText(category.Description);
        ImageView currentCategoryImage = (ImageView) convertView.findViewById(R.id.category_image);
        currentCategoryImage.setImageResource(Context.getResources().getIdentifier(category.Name.toLowerCase(),"drawable",Context.getPackageName()));

        CardView card = (CardView) convertView.findViewById(R.id.category_cardview);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TalksList fragment = FragmentHelper.GetTalksList();
                TextView tvCategory = (TextView) view.findViewById(R.id.category_name);
                fragment.SetCategory(tvCategory.getText().toString());
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
