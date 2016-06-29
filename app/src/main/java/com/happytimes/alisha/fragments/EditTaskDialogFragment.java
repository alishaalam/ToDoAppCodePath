package com.happytimes.alisha.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.happytimes.alisha.listit.R;
import com.happytimes.alisha.model.Task;

/**
 * Created by alishaalam on 6/27/16.
 */
public class EditTaskDialogFragment extends DialogFragment implements OnItemSelectedListener {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "desc";
    private static final String ARG_ID = "id";
    private static final String ARG_POS = "pos";
    private static final String ARG_PRIORITY = "priority";

    private String mTitle = "";
    private String mDesc = "";
    private String mPriority = "";
    private int mId;
    private int mPosition;


    private EditText etTitle;
    private EditText etDesc;
    private Spinner sprStatus;
    private Spinner sprPriority;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /* The activity that creates an instance of this dialog fragment must
    * implement this interface in order to receive event callbacks.
    * Each method passes the DialogFragment in case the host needs to query it. */
    public interface EditTaskDialogListener {
        void onDialogSaveClick(Task task, int position);
        void onDialogCancelClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    EditTaskDialogListener editTaskListener;

    public EditTaskDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditTaskDialogFragment newInstance(Task selectedTask, int pos) {
        EditTaskDialogFragment frag = new EditTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, selectedTask.getTitle());
        args.putString(ARG_DESC, selectedTask.getDescription());
        args.putString(ARG_PRIORITY, selectedTask.getPriority());
        args.putInt(ARG_ID, selectedTask.getId());
        args.putInt(ARG_POS, pos);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
            mDesc = getArguments().getString(ARG_DESC);
            mPriority = getArguments().getString(ARG_PRIORITY);
            mId = getArguments().getInt(ARG_ID);
            mPosition = getArguments().getInt(ARG_POS);

        }

        //Get the layout inflater for the Dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //Inflate and set the layout for the Dialog
        View layout = inflater.inflate(R.layout.fragment_dialog_edit_task, null);
        etTitle = (EditText) layout.findViewById(R.id.etTitle);
        etDesc = (EditText) layout.findViewById(R.id.etDescription);
        sprPriority = (Spinner) layout.findViewById(R.id.sprPriority);
        sprStatus = (Spinner) layout.findViewById(R.id.sprStatus);

        etTitle.setText(mTitle);
        etDesc.setText(mDesc);
        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.task_priority, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sprPriority.setAdapter(priorityAdapter);
        mPriority = sprPriority.getItemAtPosition(0).toString();

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.task_status, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sprStatus.setAdapter(statusAdapter);


        //TODO : Add support for handling spinner click

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Set the title
        builder.setTitle(R.string.dialog_title);


        // Pass null as the parent view because its going in the dialog layout
        builder.setView(layout);

        // Add action buttons
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Send the positive button back to the host activity
                Task task = new Task(mId, etTitle.getText().toString(), mPriority);
                editTaskListener.onDialogSaveClick(task, mPosition);
                Log.i("Fragment", "New Title: " + etTitle.getText().toString() + "Priority " + mPriority);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Send the negative button event back to the host activity
                editTaskListener.onDialogCancelClick(EditTaskDialogFragment.this);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            editTaskListener = (EditTaskDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement EditTaskDialogListener");
        }
    }
}
