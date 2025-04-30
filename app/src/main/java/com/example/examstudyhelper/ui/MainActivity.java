package com.example.examstudyhelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.examstudyhelper.R;
import com.example.examstudyhelper.adapters.ExamAdapter;
import com.example.examstudyhelper.database.AppDatabase;
import com.example.examstudyhelper.models.Exam;
import com.example.examstudyhelper.ui.dailogs.ExamRegistrationDialog;
import com.example.examstudyhelper.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExamAdapter examAdapter;
    private List<Exam> examList; //試験のリスト
    private AppDatabase database; //データベース関連　この辺は分からん

    private TextView todayDateTextView; // 今日の日付を表示する TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初期化
        recyclerView = findViewById(R.id.recycler_exam_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 今日の日付の表示
        todayDateTextView = findViewById(R.id.text_today_date);
        String todayDate = DateUtils.getTodayDate(); // 今日の日付を取得
        todayDateTextView.setText(todayDate); // TextView に設定

        //データベースへの保存
        database = AppDatabase.getInstance(this);
        examList = new ArrayList<>();
        
        loadExamsFromDatabase();// データをロード

        // アダプターの設定
        examAdapter = new ExamAdapter(examList, new ExamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Exam exam) {
                // 試験クリック時の処理(詳細表示)
                onExamClicked(exam);
            }

            @Override
            public void onExamLongClick(Exam exam) {
                // 試験長押し時の処理(削除)
                onExamLongClicked(exam);
            }
        });
        recyclerView.setAdapter(examAdapter);

        // 試験登録ボタンのクリックリスナー
        findViewById(R.id.button_register_exam).setOnClickListener(v -> openExamRegistrationDialog());

    }


    //実際のデータベースへのアクセス
    private void loadExamsFromDatabase() {
        new Thread(() -> {
            examList.addAll(database.examDao().getAllExams());
            runOnUiThread(() -> examAdapter.notifyDataSetChanged());
        }).start();
    }

    private void openExamRegistrationDialog() {
        ExamRegistrationDialog dialog = ExamRegistrationDialog.newInstance((examName, examDate) -> {
            // 新しい試験データを作成
            Exam newExam = new Exam(examName, examDate);

            // ローカルリストに追加
            examList.add(newExam);

            // アダプターのデータ更新
            examAdapter.notifyDataSetChanged();

            // データベースに保存
            new Thread(() -> {
                database.examDao().insertExam(newExam);
            }).start();
        });
        dialog.show(getSupportFragmentManager(), "ExamRegistrationDialog");
    }

    private void onExamClicked(Exam exam) {
        // 試験詳細画面を開く
        Intent intent = new Intent(this, ExamDetailActivity.class);
        intent.putExtra("examId", exam.getId());
        Log.d("MainActivity", "Exam ID: " + exam.getId());
        startActivity(intent);
    }

    //試験の削除(こっちは長押し)
    private void onExamLongClicked(Exam exam) {
        new AlertDialog.Builder(this)
                .setTitle("削除確認")
                .setMessage("試験「" + exam.getExamName() + "」を削除しますか？")
                .setPositiveButton("削除", (dialog, which) -> {
                    new Thread(() -> {
                        database.examDao().deleteExam(exam);
                        examList.remove(exam);
                        runOnUiThread(() -> {
                            examAdapter.notifyDataSetChanged();
                            Toast.makeText(this, "削除しました", Toast.LENGTH_SHORT).show();
                        });
                    }).start();
                })
                .setNegativeButton("キャンセル", null)
                .show();
    }
}



