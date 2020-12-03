package com.example.purpleinventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryList extends AppCompatActivity {
    private final String TAG = "CATEGORY LIST ACTIVITY";

    ListView listView;
    CategoryAdapter categoryAdapter;
    HashMap<String, List<String>> categoryDetail;
    List<String> categoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Category categories = new Category(this);
        categories.getAllData(new VolleyOnEventListener() {
            @Override
            public void onSuccess(List response) {
                Log.d(TAG, "Getting data successful");

                List<Map<String, Object>> data = response;

                categoryDetail = new HashMap<String, List<String>>();
                for(int i=0; i < data.size(); i++) {
                    String name = data.get(i).get("categoryName").toString();
                    String created = data.get(i).get("created").toString();

                    List<String> detail = new ArrayList<String>();
                    detail.add(name);
                    detail.add(created);
                    categoryDetail.put(name, detail);
                }

                listView = (ListView) findViewById(R.id.categoryListView);
                categoryTitle = new ArrayList<String>(categoryDetail.keySet());
                categoryAdapter = new CategoryAdapter(CategoryList.this, categoryTitle, categoryDetail);
                listView.setAdapter(categoryAdapter);
            }
        });
    }

    public void seeItems(View view) {
        TextView categoryItem = (TextView) view.findViewById(R.id.categoryList);
        Object tag = categoryItem.getTag();
        Log.d(TAG, tag.toString());
        Intent intent = new Intent(CategoryList.this, TakeInventory.class);
        intent.putExtra("category", tag.toString());
        startActivity(intent);
    }

    public void addCategory(View view) {
        Log.d(TAG, "adding category");

        EditText nameField = (EditText) findViewById(R.id.categoryNameText);
        String name = nameField.getText().toString();

        Category category = new Category(this);
        category.setCategoryId(name);
        category.writeData();
    }
}