package com.example.examstudyhelper.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.examstudyhelper.database.repositories.TaskRepository;
import com.example.examstudyhelper.models.Task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository taskRepository;
    private MutableLiveData<List<Task>> tasksByExamId = new MutableLiveData<>();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
    }

    public LiveData<List<Task>> getTasksByExamId(int examId) {
        executorService.execute(() -> {
            List<Task> tasks = taskRepository.getTasksByExamId(examId);
            tasksByExamId.postValue(tasks);
        });
        return tasksByExamId;
    }

    public void insertTask(Task task) {
        taskRepository.insertTask(task);
    }

    public void updateTask(Task task) {
        taskRepository.updateTask(task);
    }

    public void deleteTask(Task task) {
        taskRepository.deleteTask(task);
    }
}
