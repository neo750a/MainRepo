package com.example.arguile.todolist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

        user = new UserSimple();
        gson = new Gson();

        //get data from txt file
        //readItems();

        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        //set arrayadapter to display items like a simple list item from the array list
        //itemsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, items);
        //set the adapter to be attached to the listView
        //lvItems.setAdapter(itemsAdapter);
        //add items to the list
        //items.add("First Item");
        //items.add("Second Item");

        getJson();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_2){
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(user.getUser()[0].getName());
                text2.setText(user.getUser()[0].getAge());

                return view;
            }
        };

        lvItems.setAdapter(adapter);





        //sets up LongClickListener
        setupListViewListener();

        try {
            getAssets().open("data.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                //saveItems();
                return true;
            }
        });
    }

    //grab data from data.json
    public void getJson()
    {
        String json = "";
        //load the json file into input stream
        try {
            InputStream is = getAssets().open("data.json"); //open file to input stream
            int size = is.available();//check how much space is needed
            byte[] buffer = new byte[size];//create a byte array to store the size of the file
            is.read(buffer);//input stream writes the file byte by byte to the byte array
            is.close();//close stream

            json = new String(buffer, "UTF-8"); //assign the buffer to a string object

        } catch (IOException e) {
            e.printStackTrace();
        }

        //first param = string, second param  = type of object
        //assigns json arraylist to userSimple array list
        user = gson.fromJson(json, UserSimple.class);
        //fill the list view with the new user object

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












