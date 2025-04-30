package com.example.examstudyhelper.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.examstudyhelper.utils.DateUtils;

@Entity(tableName = "exams")
public class Exam {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String examName;
    private String examDate;

    // 引数なしのデフォルトコンストラクタ（Room用）
    public Exam() {}

    // 引数付きコンストラクタ（Room では使用しないので無視）
    @Ignore
    public Exam(String examName, String examDate) {
        this.examName = examName;
        this.examDate = examDate;
    }


    // ゲッターとセッター
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public int getRemainingDays() {
        return DateUtils.calculateDaysRemaining(this.examDate);
    }
}


