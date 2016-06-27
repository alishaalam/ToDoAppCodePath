package com.happytimes.alisha.listit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.happytimes.alisha.database.Task;
import com.happytimes.alisha.database.TaskReaderDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends AppCompatActivity {

    List<Task> taskList;
    ArrayAdapter<Task> taskAdapter;
    ListView listView;

    static final String TAG = "MainActivity";
    static final int EDIT_ITEM_REQUEST = 1; //request code
    static final String ITEM_POSITION = "com.happytimes.alisha.listit.ITEM_POSITION";
    static final String ITEM_NAME = "com.happytimes.alisha.listit.ITEM_NAME";

    TaskReaderDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = TaskReaderDatabaseHelper.getInstance(this);
        listView = (ListView) findViewById(R.id.lvItems);
        taskList = new ArrayList<>();
        //taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
        //listView.setAdapter(taskAdapter);
        setUpListViewListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFromDB();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                taskList.remove(position);
                Task selectedTask = (Task) parent.getItemAtPosition(position);
                dbHelper.deleteTask(selectedTask.getId());
                taskAdapter.notifyDataSetChanged();
                return true;
            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Task selectedTask = (Task) parent.getItemAtPosition(position);
                Intent intent = new Intent(TodoActivity.this, EditItemActivity.class);
                intent.putExtra(ITEM_POSITION, position);
                intent.putExtra(ITEM_NAME, selectedTask.getTitle());
                Log.i("MainActivity: ", " Item: " + selectedTask);
                Log.i("MainActivity: ", "Position: " + position);
                startActivityForResult(intent, EDIT_ITEM_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_ITEM_REQUEST && resultCode == RESULT_OK) {
            String editedTitle = data.getExtras().getString(EditItemActivity.EDITED_ITEM);
            int editedPosition = data.getExtras().getInt(EditItemActivity.EDITED_POSITION);
            Log.i("MainActivity: ", " Returned Position: " + editedPosition);
            Log.i("MainActivity: ", " Returned Item: " + editedTitle);

            Task editedTask = new Task(editedTitle, "Returned Desc");

            if (editedPosition != -1) {
                taskList.set(editedPosition, editedTask);
            }

            taskAdapter.notifyDataSetChanged();
            updateTaskDB(editedTask);
        }
    }

    private void updateTaskDB(Task task) {
        int updatedId = dbHelper.updateTask(task);
        Log.d(TAG, "Updated row Id" + updatedId);
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String taskTitle = etNewItem.getText().toString();
        Task t = new Task(taskTitle, "Task Desc");
        taskList.add(t);
        etNewItem.setText("");
        writeToDB(t);
    }


    private void loadFromDB() {
        List<Task> list = dbHelper.getAllTasks();

        taskList.clear();
        taskList.addAll(list);

        Log.d(TAG, "List size" + taskList.size());
        if(taskAdapter == null){
            taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
        }

        listView.setAdapter(taskAdapter);
        //taskAdapter.setItems(taskList);
        taskAdapter.notifyDataSetChanged();

        for (Task task : taskList) {
            String log = "Id: " + task.getId() + ", title: " + task.getTitle() + ", Desc: " + task.getDescription();
            Log.d(TAG, log);
        }
    }

    private void writeToDB(Task task) {
        long newRowId = dbHelper.insertTask(task);
        Log.d(TAG, "Inserted row Id" + newRowId);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_add:
                onAddItem(item.getActionView());
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
