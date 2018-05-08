package com.example.nickomarsellino.scheduling;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener listener;

    int Dirs;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);

        Dirs = swipeDirs;
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if(listener != null){
            listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder != null){
                View listDataView = ((ScheduleAdapter.ViewHolder)viewHolder).viewListData;
                getDefaultUIUtil().onSelected(listDataView);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (Dirs == 4){
            View swipeLeft = ((ScheduleAdapter.ViewHolder)viewHolder).viewSwipeLeft;
            getDefaultUIUtil().clearView(swipeLeft);

        }
        else if(Dirs == 8){
            View swipeRight = ((ScheduleAdapter.ViewHolder)viewHolder).viewSwipeRight;
            getDefaultUIUtil().clearView(swipeRight);

        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (Dirs == 4){
            View swipeLeft = ((ScheduleAdapter.ViewHolder)viewHolder).viewSwipeLeft;
            getDefaultUIUtil().clearView(swipeLeft);

            View listDataView = ((ScheduleAdapter.ViewHolder)viewHolder).viewListData;
            getDefaultUIUtil().onDraw(c, recyclerView, listDataView, dX, dY, actionState, isCurrentlyActive);

        }
        else if(Dirs == 8){
            View swipeRight = ((ScheduleAdapter.ViewHolder)viewHolder).viewSwipeRight;
            getDefaultUIUtil().clearView(swipeRight);

            View listDataView = ((ScheduleAdapter.ViewHolder)viewHolder).viewListData;
            getDefaultUIUtil().onDraw(c, recyclerView, listDataView, dX, dY, actionState, isCurrentlyActive);

        }
    }
}
