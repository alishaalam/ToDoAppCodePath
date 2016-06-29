package com.happytimes.alisha.helper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.happytimes.alisha.listit.TaskRecyclerAdapter;

/**
 * Created by alishaalam on 6/29/16.
 */
public class CardTouchHelper extends ItemTouchHelper.SimpleCallback {

    TaskRecyclerAdapter taskRecyclerAdapter;

    public CardTouchHelper(TaskRecyclerAdapter taskRecyclerAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN,  ItemTouchHelper.RIGHT);
        this.taskRecyclerAdapter = taskRecyclerAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        taskRecyclerAdapter.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        taskRecyclerAdapter.remove(viewHolder.getAdapterPosition());
    }
}
