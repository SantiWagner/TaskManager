package com.wagner.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.wagner.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TaskAdapter taskAdapter;
    RecyclerView taskRecyclerView;
    List<Task> tasks = new ArrayList<>();

    private int PRIORITY_FILTER = 0;
    private boolean STATUS_FILTER = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskAdapter = new TaskAdapter(this, tasks);

        taskRecyclerView = (RecyclerView) findViewById(R.id.task_list);

        final GridLayoutManager itemsLayoutManager = new GridLayoutManager(this, 1);

        taskRecyclerView.setLayoutManager(itemsLayoutManager);

        taskRecyclerView.setAdapter(taskAdapter);

        findViewById(R.id.addTaskFloatingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
                startActivity(intent);
            }
        });



        tasks.clear();




        ((Spinner)findViewById(R.id.spinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    PRIORITY_FILTER = position;
                updateListAfterFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        ((Spinner)findViewById(R.id.spinner4)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                STATUS_FILTER = position == 1;
                updateListAfterFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        tasks.addAll(AppDatabase.getInstance(this).taskDao().getAll());
        taskAdapter.notifyDataSetChanged();
    }

    public  void updateListAfterFilter(){
        tasks.clear();

        if(PRIORITY_FILTER == 0){
            tasks.addAll(AppDatabase.getInstance(this).taskDao().getAllByState(STATUS_FILTER));
            taskAdapter.notifyDataSetChanged();
            return;
        }

        tasks.addAll(AppDatabase.getInstance(this).taskDao().getAllByPriorityByState(PRIORITY_FILTER,STATUS_FILTER));
        taskAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    public void checkTask(View v){
        int task_id=v.getId();
        CheckBox view=(CheckBox)v;

        if(view.isChecked())
            ((TextView)((View)v.getParent().getParent()).findViewById(R.id.task_title)).setPaintFlags(((TextView)((View)v.getParent().getParent()).findViewById(R.id.task_title)).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else
            ((TextView)((View)v.getParent().getParent()).findViewById(R.id.task_title)).setPaintFlags(((TextView)((View)v.getParent().getParent()).findViewById(R.id.task_title)).getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        int i=0;
        for(Task t:tasks)
        {
            if(t.getId()==task_id) {
                t.setCompleted(view.isChecked());
                AppDatabase.getInstance(this).taskDao().updateTask(t);
                hideItem((View)v.getParent().getParent(),t);
            }
            i++;
        }
    }

    private void hideItem(final View rowView,  final Task t) {

        final Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                android.R.anim.slide_out_right);

        anim.setDuration(300);


        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                rowView.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        tasks.remove(t);
                        taskAdapter.notifyDataSetChanged(); //Refresh list
                    }

                }, anim.getDuration());
            }
        }, 500);

    }


        @Override
    public void onResume(){
        super.onResume();

        updateListAfterFilter();

    }


}
