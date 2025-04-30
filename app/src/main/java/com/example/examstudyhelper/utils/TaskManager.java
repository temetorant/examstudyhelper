package com.example.examstudyhelper.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.examstudyhelper.adapters.TaskAdapter;
import com.example.examstudyhelper.database.AppDatabase;
import com.example.examstudyhelper.models.Task;
import com.github.mikephil.charting.charts.BarChart;

import java.util.List;
import java.util.concurrent.ExecutorService;

import java.util.concurrent.Executors;

public class TaskManager {
    private final AppDatabase database;
    private final ExecutorService executorService;
    private final int examId;
    private final Runnable updateChartCallback;

    public TaskManager(Context context, int examId, Runnable updateChartCallback) {
        this.database = AppDatabase.getInstance(context);
        this.executorService = Executors.newSingleThreadExecutor();
        this.examId = examId;
        this.updateChartCallback = updateChartCallback;
    }

    public interface TaskLoadCallback {
        void onTasksLoaded(List<Task> tasks);
    }

    public void loadTasks(int examId, TaskLoadCallback callback) {
        executorService.execute(() -> {
            List<Task> tasks = database.taskDao().getTasksByExamId(examId);
            //DBからタスクを取得
            callback.onTasksLoaded(tasks);
            //結果を返す
        });
    }

    //タスクをDBに追加
    public void addTask(Task task, List<Task> taskList, TaskAdapter taskAdapter) {
        executorService.execute(() -> {
            database.taskDao().insertTask(task);
            taskList.add(task);

            // メインスレッドで UI 更新
            new Handler(Looper.getMainLooper()).post(() -> {
                taskAdapter.notifyItemInserted(taskList.size() - 1);
                updateChartCallback.run();
            });
        });
    }
    //完了状態の更新
    public void updateTaskCompletion(Task task, boolean isCompleted, Runnable callback) {
        executorService.execute(() -> {
            task.setCompleted(isCompleted);
            database.taskDao().updateTask(task);
            callback.run(); // グラフ更新などの処理を実行
        });
    }

    //DBから削除
    public void deleteTask(Task task, List<Task> taskList, TaskAdapter taskAdapter, Runnable callback) {
        executorService.execute(() -> {
            database.taskDao().deleteTask(task);

            // メインスレッドで UI 更新
            new Handler(Looper.getMainLooper()).post(() -> {
                taskList.remove(task);
                taskAdapter.notifyDataSetChanged(); // 一覧全体をリフレッシュ
                callback.run();
            });
        });
    }
}


