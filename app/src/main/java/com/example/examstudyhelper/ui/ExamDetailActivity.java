package com.example.examstudyhelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examstudyhelper.R;
import com.example.examstudyhelper.adapters.TaskAdapter;
import com.example.examstudyhelper.database.AppDatabase;
import com.example.examstudyhelper.models.Exam;
import com.example.examstudyhelper.models.Task;
import com.example.examstudyhelper.ui.dailogs.TaskDetailsDialog;
import com.example.examstudyhelper.ui.dailogs.TaskRegistrationDialog;
import com.example.examstudyhelper.utils.DateUtils;
import com.example.examstudyhelper.utils.TaskManager;
import com.example.examstudyhelper.utils.TaskChartHelper;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.List;

public class ExamDetailActivity extends AppCompatActivity {

    private TextView examNameTextView;
    private TextView examDateTextView;
    private TextView remainingDaysTextView;
    private RecyclerView tasksRecyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private BarChart barChart;
    private TaskManager taskManager;
    private TaskChartHelper taskChartHelper;
    private int examId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_detail);
        //画面レイアウトをセット

        initViews(); //UII部品の取得とリスナーの設定
        setupRecyclerView();

        examId = getIntent().getIntExtra("examId", -1);
        if (examId == -1) {
            Toast.makeText(this, "試験IDが無効です", Toast.LENGTH_SHORT).show();
            finish(); // ここで Activity を終了させる
        }

        //初期化
        taskManager = new TaskManager(this, examId, this::updateBarChart);
        taskChartHelper = new TaskChartHelper(this, barChart);

        if (examId != -1) {
            loadExamDetails(examId);
        } else {
            Toast.makeText(this, "試験IDが無効です", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        examNameTextView = findViewById(R.id.tvExamName);
        examDateTextView = findViewById(R.id.tvExamDate);
        remainingDaysTextView = findViewById(R.id.text_view_remaining_days);
        tasksRecyclerView = findViewById(R.id.rvTaskList);
        barChart = findViewById(R.id.barChart);

        //タスク追加
        Button addTaskButton = findViewById(R.id.btnAddTask);
        addTaskButton.setOnClickListener(v -> openTaskRegistrationDialog());

        //ホームに戻る
        Button goToHomeButton = findViewById(R.id.btnGoToHome);
        goToHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ExamDetailActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            //finish();　なんかあるとダメっぽい？
        });
    }

    private void setupRecyclerView() {
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(
                taskList,
                (task, isComplete) -> {
                    //変更
                    taskManager.updateTaskCompletion(task, isComplete, this::updateBarChart);
                },
                task -> {
                    //削除
                    taskManager.deleteTask(task, taskList, taskAdapter, this::updateBarChart);
                },
                    //詳細表示
                task -> openTaskDetailsDialog(task.getId())
        );
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksRecyclerView.setAdapter(taskAdapter);
    }

    private void updateBarChart() {
        taskChartHelper.updateBarChart(taskList);
        // TaskChartHelper で使う
    }

    //試験情報の取得
    private void loadExamDetails(int examId) {
        new Thread(() -> {
            Exam exam = AppDatabase.getInstance(this).examDao().getExamById(examId);
            runOnUiThread(() -> {
                if (exam != null) {
                    examNameTextView.setText(exam.getExamName());
                    examDateTextView.setText(exam.getExamDate());

                    //残り日数
                    int remainingDays = DateUtils.calculateDaysRemaining(exam.getExamDate());
                    remainingDaysTextView.setText(String.format("残り%d日", remainingDays));
                    taskManager.loadTasks(examId, tasks -> {
                        taskList.clear();
                        taskList.addAll(tasks);
                        taskAdapter.notifyDataSetChanged();
                        updateBarChart();
                    });
                } else {
                    Toast.makeText(this, "試験データが見つかりません", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    //新規タスクの登録
    private void openTaskRegistrationDialog() {
        TaskRegistrationDialog dialog = TaskRegistrationDialog.newInstance(examId, task -> {
            taskManager.addTask(task, taskList, taskAdapter);
        });
        dialog.show(getSupportFragmentManager(), "TaskRegistrationDialog");
    }

    //タスク詳細画面だけど、要らないかも
    private void openTaskDetailsDialog(int taskId) {
        new Thread(() -> {
            Task task = AppDatabase.getInstance(this).taskDao().getTaskById(taskId);
            runOnUiThread(() -> {
                if (task != null) {
                    TaskDetailsDialog dialog = TaskDetailsDialog.newInstance(task);
                    dialog.show(getSupportFragmentManager(), "TaskDetailsDialog");
                } else {
                    Toast.makeText(this, "タスクが見つかりません", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
