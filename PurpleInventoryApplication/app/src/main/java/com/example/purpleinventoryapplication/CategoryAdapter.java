package com.example.purpleinventoryapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class CategoryAdapter extends TakeInventoryAdapter{

    private Context context;
    private List<String> listTitle;
    private HashMap<String, List<String>> listDetail;

    public CategoryAdapter(Context context, List<String> listTitle, HashMap<String, List<String>> listDetail) {
        super(context, listTitle, listDetail);
        this.context = context;
        this.listTitle = listTitle;
        this.listDetail = listDetail;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_category, null);
        }

        List<String> item = getDetails(listTitle);
        String categoryName = item.get(0);

        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.categoryList);
        listTitleTextView.setTag(categoryName);
        listTitleTextView.setText(listTitle);

        return convertView;
    }
}
