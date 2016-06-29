package com.happytimes.alisha.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alishaalam on 6/25/16.
 */
public class TaskReaderDatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "TaskReader.db";
    private static final int DATABASE_VERSION = 1;
    private static TaskReaderDatabaseHelper sInstance;


    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = " , ";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TaskReaderContract.TaskEntry.TABLE_NAME + " (" +
                    TaskReaderContract.TaskEntry.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    TaskReaderContract.TaskEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    TaskReaderContract.TaskEntry.COLUMN_NAME_DEADLINE + TEXT_TYPE + COMMA_SEP +
                    TaskReaderContract.TaskEntry.COLUMN_NAME_PRIORITY + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskReaderContract.TaskEntry.TABLE_NAME;


    public static synchronized TaskReaderDatabaseHelper getInstance(Context ctx) {
        if(sInstance == null)
            sInstance = new TaskReaderDatabaseHelper(ctx.getApplicationContext());

        return sInstance;
    }

    private TaskReaderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public long insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskReaderContract.TaskEntry.COLUMN_NAME_PRIORITY, task.getPriority());


        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                TaskReaderContract.TaskEntry.TABLE_NAME, null, values);
        //The first argument for insert() is simply the table name.
        // The second argument provides the name of a column in which the framework can insert NULL in the event
        // that the ContentValues is empty (if you instead set this to "null", then the framework will not insert
        // a row when there are no values)
        return newRowId;
    }

    public Task getTask(int rowId) {

        SQLiteDatabase db = this.getReadableDatabase();
        //SQLiteDatabase db = sInstance.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                TaskReaderContract.TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE,
                TaskReaderContract.TaskEntry.COLUMN_NAME_DESCRIPTION };

        // Define 'where' part of query.
        String selection = TaskReaderContract.TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ? ";

        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(rowId) };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE + " DESC";

        Cursor cursor = db.query(
                TaskReaderContract.TaskEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if(cursor != null) {
            cursor.moveToNext();
        }

        Task task = new Task();
        task.setId(Integer.parseInt(cursor.getString(0)));
        task.setTitle(cursor.getString(1));
        task.setDescription(cursor.getString(2));

        return task;
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<Task>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TaskReaderContract.TaskEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setTitle(cursor.getString(1));
                task.setPriority(cursor.getString(4));
                // Adding task to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        // return task list
        return taskList;

    }

    // Getting task Count
    public int getTasksCount() {
        String countQuery = "SELECT  * FROM " + TaskReaderContract.TaskEntry.TABLE_NAME;
        SQLiteDatabase db = sInstance.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating single task
    public int updateTask(Task task) {
        SQLiteDatabase db = sInstance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskReaderContract.TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());

        // updating row
        return db.update(TaskReaderContract.TaskEntry.TABLE_NAME, values, TaskReaderContract.TaskEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[] { String.valueOf(task.getId()) });
    }

    public void deleteTask(int rowId) {
        //SQLiteDatabase db = mDbHelper.getReadableDatabase();
        SQLiteDatabase db = sInstance.getReadableDatabase();

        // Define 'where' part of query.
        String selection = TaskReaderContract.TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ? ";

        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(rowId) };

        // Issue SQL statement.
        db.delete(TaskReaderContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
    }

}
