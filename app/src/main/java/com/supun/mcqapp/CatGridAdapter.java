package com.supun.mcqapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CatGridAdapter extends BaseAdapter {
    private List<CategoryModel> catList;

    public CatGridAdapter(List<CategoryModel> catList) {
        this.catList = catList;
    }

    @Override
    public int getCount() {
        return catList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if (convertView == null)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_layout,parent,false);
        }
        else
        {
            view = convertView;
        }

        //position ekata kalin putextra ekak tibba.eeka poddak balanna.awlak giyoth.

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //MainActivity.selected_cat_index = position;
                Intent intent = new Intent(parent.getContext(),SetsActivity.class);
                intent.putExtra("CATEGORY_ID",position + 1);
                parent.getContext().startActivity(intent);
            }
        });

        ((TextView) view.findViewById(R.id.cat_topic)).setText(catList.get(position).getName());

        return view;
    }
}
