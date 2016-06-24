package com.happytimes.alisha.listit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    static final int EDIT_ITEM_REQUEST = 1; //request code
    static final String ITEM_POSITION = "com.happytimes.alisha.listit.ITEM_POSITION";
    static final String ITEM_NAME = "com.happytimes.alisha.listit.ITEM_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        setUpListViewListener();
    }

    private void setUpListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }

        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra(ITEM_POSITION, position);
                intent.putExtra(ITEM_NAME, item);
                Log.i("MainActivity: ", " Item: " + item);
                Log.i("MainActivity: ", "Position: " + position);
                startActivityForResult(intent, EDIT_ITEM_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT_ITEM_REQUEST && resultCode == RESULT_OK) {
            String editedItem = data.getExtras().getString(EditItemActivity.EDITED_ITEM);
            int editedPosition = data.getExtras().getInt(EditItemActivity.EDITED_POSITION);
            Log.i("MainActivity: ", " Returned Item: " + editedItem);
            Log.i("MainActivity: ", " Returned Position: " + editedPosition);
            if(editedPosition != -1) {
                items.set(editedPosition,editedItem);
            }
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String newItem = etNewItem.getText().toString();
        items.add(newItem);
        etNewItem.setText("");
        writeItems();
    }


    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "listit.txt");
        try{
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "listit.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
