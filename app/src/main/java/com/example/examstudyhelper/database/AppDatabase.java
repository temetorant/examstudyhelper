package com.example.examstudyhelper.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;

import com.example.examstudyhelper.models.Exam;
import com.example.examstudyhelper.database.dao.ExamDao;
import com.example.examstudyhelper.models.Task;
import com.example.examstudyhelper.database.dao.TaskDao;

@Database(entities = {Exam.class, Task.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ExamDao examDao();
    public abstract TaskDao taskDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "exam_study_helper_db")
                    .fallbackToDestructiveMigration()// データベースをリセット
                    .build();
        }
        return instance;
    }

    // マイグレーションの定義
    //一応、残しておく
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // スキーマ変更のSQLを記述
            database.execSQL("ALTER TABLE exams ADD COLUMN new_column_name TEXT");
        }
    };
}

