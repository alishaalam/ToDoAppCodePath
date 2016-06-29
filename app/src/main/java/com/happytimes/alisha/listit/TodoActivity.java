package com.happytimes.alisha.listit;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.happytimes.alisha.fragments.EditTaskDialogFragment;
import com.happytimes.alisha.helper.CardTouchHelper;
import com.happytimes.alisha.model.Task;
import com.happytimes.alisha.model.TaskReaderDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends AppCompatActivity implements EditTaskDialogFragment.EditTaskDialogListener {

    List<Task> taskList;
    TaskRecyclerAdapter taskAdapter;
    RecyclerView recyclerListView;

    static final String TAG = "TodoActivity";
   /* static final int EDIT_ITEM_REQUEST = 1; //request code
    static final String ITEM_POSITION = "com.happytimes.alisha.listit.ITEM_POSITION";
    static final String ITEM_NAME = "com.happytimes.alisha.listit.ITEM_NAME";*/

    TaskReaderDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = TaskReaderDatabaseHelper.getInstance(this);

        setupToolbar();

        taskList = new ArrayList<>();

        recyclerListView = (RecyclerView) findViewById(R.id.cardList);
        recyclerListView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerListView.setLayoutManager(linearLayoutManager);
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (taskAdapter == null) {

            taskAdapter = new TaskRecyclerAdapter(taskList, new TaskRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Task task, int position) {
                    //Toast.makeText(TodoActivity.this, "Task touched!" + task.getTitle(), Toast.LENGTH_SHORT).show();
                    showEditTaskDialog(task, position);
                }
            });
        }
        recyclerListView.setAdapter(taskAdapter);


        loadFromDB();
        setupCardTouchHelper();
    }

    private void setupCardTouchHelper() {
        ItemTouchHelper.Callback callback = new CardTouchHelper(taskAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerListView);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /*private void setUpListViewListener() {
        recyclerListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Task selectedTask = (Task) parent.getItemAtPosition(position);
                dbHelper.deleteTask(selectedTask.getId());

                taskList.remove(position);
                taskAdapter.notifyDataSetChanged();

                return true;
            }

        });

        recyclerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Task selectedTask = (Task) parent.getItemAtPosition(position);
                showEditTaskDialog(selectedTask, position);
            }
        });
    }*/

    private void showEditTaskDialog(Task selectedTask, int position) {
        FragmentManager fm = getSupportFragmentManager();
        EditTaskDialogFragment alertDialog = EditTaskDialogFragment.newInstance(selectedTask, position);
        alertDialog.show(fm, TAG);

    }


    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String taskTitle = etNewItem.getText().toString();
        Task t = new Task(taskTitle, "Normal");
        taskList.add(t);
        taskAdapter.notifyDataSetChanged();
        etNewItem.setText("");
        writeToDB(t);
    }


    private void loadFromDB() {
        List<Task> list = dbHelper.getAllTasks();
        taskList.clear();
        taskList.addAll(list);

        Log.d(TAG, "List size" + taskList.size());
        taskAdapter.notifyDataSetChanged();

        for (Task task : taskList) {
            String log = "Id: " + task.getId() + ", title: " + task.getTitle() + ", Desc: " + task.getDescription();
            Log.d(TAG, log);
        }
    }

    private long writeToDB(Task task) {
        long newRowId = dbHelper.insertTask(task);
        Log.d(TAG, "Inserted row Id" + newRowId);
        return newRowId;
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


    // This method is invoked in the activity when the listener is triggered
    // Access the data result passed to the activity here
    @Override
    public void onDialogSaveClick(Task editedTask, int position) {
        dbHelper.updateTask(editedTask);

        taskList.set(position, editedTask);
        taskAdapter.notifyDataSetChanged();
    }

    private void updateTaskDB(Task task) {
        int updatedId = dbHelper.updateTask(task);
        Log.d(TAG, "Updated row Id" + updatedId);
    }

    @Override
    public void onDialogCancelClick(DialogFragment dialog) {
        dialog.dismiss();
    }


}
