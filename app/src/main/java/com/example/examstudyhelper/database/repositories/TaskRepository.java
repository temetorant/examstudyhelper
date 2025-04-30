package com.example.examstudyhelper.database.repositories;

import android.content.Context;

import com.example.examstudyhelper.database.dao.TaskDao;
import com.example.examstudyhelper.database.AppDatabase;
import com.example.examstudyhelper.models.Task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepository {
    private TaskDao taskDao;
    private ExecutorService executorService;

    public TaskRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        taskDao = db.taskDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertTask(Task task) {
        executorService.execute(() -> taskDao.insertTask(task));
    }

    public void updateTask(Task task) {
        executorService.execute(() -> taskDao.updateTask(task));
    }

    public void deleteTask(Task task) {
        executorService.execute(() -> taskDao.deleteTask(task));
    }

    public List<Task> getTasksByExamId(int examId) {
        return taskDao.getTasksByExamId(examId);
    }
    //タスク一覧を返す
    //これも非同期で出来るか？　要改良案
}
