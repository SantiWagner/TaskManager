package com.wagner.taskmanager;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.wagner.taskmanager.model.Task;

import java.util.List;

/**
 * Created by Santiago on 2/23/2019.
 */

@Dao
public interface TaskDao {


    @Query("SELECT * FROM tasks ORDER BY CASE WHEN completed THEN 1 ELSE 0 END, priority DESC, dueDate ASC")
    List<Task> getAll();

    @Query("SELECT * FROM tasks WHERE completed = :isCompleted ORDER BY CASE WHEN completed THEN 1 ELSE 0 END, priority DESC, dueDate ASC")
    List<Task> getAllByState(boolean isCompleted);

    @Query("SELECT * FROM tasks WHERE priority = :priority ORDER BY CASE WHEN completed THEN 1 ELSE 0 END, priority DESC, dueDate ASC")
    List<Task> getAllByPriority(int priority);

    @Query("SELECT * FROM tasks WHERE priority = :priority AND completed = :isCompleted ORDER BY CASE WHEN completed THEN 1 ELSE 0 END, priority DESC, dueDate ASC")
    List<Task> getAllByPriorityByState(int priority, boolean isCompleted);

    @Insert
    void insertTask(Task tasks);

    @Delete
    void delete(Task task);

    @Update
    void updateTask(Task task);
}

