package com.happytimes.alisha.listit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.happytimes.alisha.model.Task;

import java.util.Collections;
import java.util.List;

/**
 * Created by alishaalam on 6/29/16.
 */
public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Task task, int position);
    }

    private List<Task> tasksRecList;
    Context context;
    private final OnItemClickListener listener;

    public TaskRecyclerAdapter(List<Task> taskList, OnItemClickListener listener) {
        this.tasksRecList = taskList;
        this.listener = listener;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.card_task_list_item, parent, false);

        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder taskViewHolder, int position) {
        Task task = tasksRecList.get(position);
        //taskViewHolder.vTitle.setText(task.getTitle());
        //taskViewHolder.vDesc.setText(task.getDescription());
        taskViewHolder.vPriority.setText(task.getPriority());
        //taskViewHolder.vDeadline.setText(task.getDeadline().toString());
        taskViewHolder.bind(task, listener, position);
    }

    @Override
    public int getItemCount() {
        return tasksRecList.size();
    }


    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        protected TextView vTitle;
        protected TextView vDesc;
        protected TextView vPriority;
        protected TextView vDeadline;

        public TaskViewHolder(View v) {
            super(v);
            vTitle = (TextView) v.findViewById(R.id.tvTaskTitle);
            //vDesc = (TextView) v.findViewById(R.id.tvTaskDescription);
            vPriority = (TextView) v.findViewById(R.id.tvTaskPriority);
            //vDeadline = (TextView) v.findViewById(R.id.tvTaskDeadline);
        }

        public void bind(final Task task, final OnItemClickListener listener, final int position) {
            vTitle.setText(task.getTitle());
            vPriority.setText(task.getPriority());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(task, position);
                }
            });
        }
    }

    /**Called when card is swiped right to dismiss*/
    public void remove(int position) {
            tasksRecList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Task deleted!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when cards are dragged to reorder*/
    public void swap(int orgPosition, int targetPosition){
        Collections.swap(tasksRecList, orgPosition, targetPosition);
        notifyItemMoved(orgPosition, targetPosition);
    }
}
