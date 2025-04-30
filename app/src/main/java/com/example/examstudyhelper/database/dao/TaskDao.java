package com.example.examstudyhelper.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.examstudyhelper.models.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("SELECT * FROM tasks")
    List<Task> getAllTasks();

    //idでタスクを取得する
    @Query("SELECT * FROM tasks WHERE id = :id")
    Task getTaskById(int id);

    @Query("SELECT * FROM tasks WHERE isCompleted = :status")
    List<Task> getTasksByCompletionStatus(boolean status);

    // examId を指定してタスクを取得する
    @Query("SELECT * FROM tasks WHERE examId = :examId")
    List<Task> getTasksByExamId(int examId);
}


