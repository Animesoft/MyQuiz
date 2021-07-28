package com.supun.mcqapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SetsAdapter extends BaseAdapter {

    //check
    public List<String> setList;


    private int numOfSets;

    public SetsAdapter(int numOfSets) {
        this.numOfSets = numOfSets;
    }

    //num of set>changed

    @Override
    public int getCount() {
        return numOfSets;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //final
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View view;

        if (convertView == null)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sets_item_layout,parent,false);
        }
        else
        {
            view = convertView;
        }

        //postion+1 tibbe.mn thama eka demme nee.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(),QuestionsActivity.class);
                intent.putExtra("SETNO", position + 1);
                parent.getContext().startActivity(intent);
            }
        });

        ((TextView) view.findViewById(R.id.set_number)).setText(String.valueOf(position + 1));
        return view;
    }
}
