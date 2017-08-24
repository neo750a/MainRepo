package com.example.arguile.todolist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lvItems;
    List<String> items;
    ArrayAdapter<String> itemsAdapter;

    UserSimple user;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get data from txt file
        readItems();

        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        //set arrayadapter to display items like a simple list item from the array list
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        //set the adapter to be attached to the listView
        lvItems.setAdapter(itemsAdapter);
        //add items to the list
        items.add("First Item");
        items.add("Second Item");

        user = new UserSimple();
        gson = new Gson();

        //sets up LongClickListener
        setupListViewListener();
    }

    private void setupListViewListener()
    {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long rowId) {
                //removes long clicked item in the list at position
                items.remove(position);
                //refreshes the adapter
                itemsAdapter.notifyDataSetChanged();
                //write to file when list item is removed
                saveItems();
                return true;
            }
        });
    }

    //add button method
    public void addTodoItem(View v)
    {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        itemsAdapter.add(etNewItem.getText().toString());
        etNewItem.setText("");
        //once new item is added to list, write to file
        saveItems();
    }

    //readItems and saveItems uses Commons IO library for saving and writing to txt files

    private void readItems()
    {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            //set the list to the data from the text file
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch(IOException e)
        {
            //if there is an error, set the list as new array list thats empty
            items = new ArrayList<String>();
            //send error to error log
            e.printStackTrace();
        }
    }

    private void saveItems()
    {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

}












