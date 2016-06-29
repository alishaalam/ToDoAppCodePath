package com.happytimes.alisha.model;

import android.provider.BaseColumns;

/**
 * Created by alishaalam on 6/26/16.
 */
public class TaskReaderContract {

    public TaskReaderContract(){

    }

    public static abstract class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_DEADLINE = "deadline";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_NULLABLE = "nullHack";
    }
}
