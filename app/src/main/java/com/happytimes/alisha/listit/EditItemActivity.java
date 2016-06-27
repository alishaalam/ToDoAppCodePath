package com.happytimes.alisha.listit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    static final String EDITED_ITEM = "com.happytimes.alisha.listit.EDITED_ITEM";
    static final String EDITED_POSITION = "com.happytimes.alisha.listit.EDITED_POSITION";
    EditText etEditItem;
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String item = getIntent().getStringExtra(TodoActivity.ITEM_NAME);
        position = getIntent().getIntExtra(TodoActivity.ITEM_POSITION, 0);
        
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.append(item);
        etEditItem.requestFocus();
    }

    public void onSaveItem(View view) {
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        Intent returnData = new Intent();
        returnData.putExtra(EditItemActivity.EDITED_ITEM, etEditItem.getText().toString());
        returnData.putExtra(EditItemActivity.EDITED_POSITION, position);
        setResult(RESULT_OK, returnData);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        this.finish();
    }
}
