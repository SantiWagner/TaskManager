package com.wagner.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.wagner.taskmanager.model.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();

    boolean dateSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void addNewTask(View v){
        String task_name = ((EditText)findViewById(R.id.taskNameEditText)).getText().toString();

        if(task_name.isEmpty())
        {
            Snackbar msg = Snackbar.make(this.getCurrentFocus(), "Task name can't be empty!",
                    Snackbar.LENGTH_SHORT);

            TextView tv = (TextView) (msg.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
            tv.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.catamaran_light));
            msg.show();
            return;
        }
        int priority = ((Spinner)findViewById(R.id.prioritySpinner)).getSelectedItemPosition()+1;
        String additional_details = ((EditText)findViewById(R.id.additionalDestailsTextView)).getText().toString();
        AppDatabase.getInstance(this).taskDao().insertTask(new Task(task_name, additional_details, (dateSet) ? myCalendar :  null,priority,false));
        finish();
    }

    public void showDateDialog(View v){


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateSet = true;
                ((TextView)findViewById(R.id.dateInputTextView)).setText((myCalendar == null) ? "Not set" : format.format(myCalendar.getTime()));
            }

        };
        DatePickerDialog dateDialog = new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        Calendar today = Calendar.getInstance();
        dateDialog.getDatePicker().setMinDate(today.getTimeInMillis());
        dateDialog.show();
    }

    public void goBack(View v){
        finish();
    }
}
