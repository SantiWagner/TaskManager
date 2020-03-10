package com.wagner.taskmanager;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.wagner.taskmanager.model.Task;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{


    private Context context;
    private LayoutInflater inflater;
    List<Task> data= Collections.emptyList();

    public TaskAdapter(Context context, List<Task> data){
        this.context=context;
        this.data=data;
    }

    public void clear(){
        this.data.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        ViewHolder myHolder= (ViewHolder) holder;
        Task current=data.get(position);
        myHolder.title.setText(current.getName());
        myHolder.date.setText((current.getDueDate() == null) ? "Not set" : format.format(current.getDueDate().getTime()));
        myHolder.completed.setChecked(current.isCompleted());
        myHolder.completed.setId(current.getId());

        if(!current.getDetails().isEmpty())
            myHolder.details.setText(current.getDetails());
        else {
            myHolder.details.setVisibility(View.GONE);
            myHolder.details_divider.setVisibility(View.GONE);
        }

        switch (current.getPriority()){
            case 1:
            {
                myHolder.priority.setText("Low priority");
                break;
            }
            case 2:
            {
                myHolder.priority.setText("Medium priority");
                break;
            }
            case 3:
            {
                myHolder.priority.setText("High priority");
                break;
            }
        }

        if(current.isCompleted())
            myHolder.title.setPaintFlags(myHolder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else
            myHolder.title.setPaintFlags(myHolder.title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

// Inside this class, we’ll have another class for the ViewHolder thus

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView id;
        TextView title;
        TextView date;
        TextView priority;
        TextView details;
        CheckBox completed;
        View details_divider;


        // create constructor to get widget reference
        public ViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.task_title);
            date=(TextView)itemView.findViewById(R.id.task_date);
            priority=(TextView)itemView.findViewById(R.id.task_priority);
            completed=itemView.findViewById(R.id.task_checkbox);
            details=(TextView)itemView.findViewById(R.id.task_details);
            details_divider = itemView.findViewById(R.id.details_divider);
        }
    }
}