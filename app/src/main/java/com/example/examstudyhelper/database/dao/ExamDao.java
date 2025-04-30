package com.example.examstudyhelper.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.examstudyhelper.models.Exam;

import java.util.List;

@Dao
public interface ExamDao {
    @Insert
    void insertExam(Exam exam);

    //使うから消さない
    @Update
    void updateExam(Exam exam);

    @Delete
    void deleteExam(Exam exam);

    @Query("SELECT * FROM exams")
    List<Exam> getAllExams();

    @Query("SELECT * FROM exams WHERE id = :examId")
    Exam getExamById(int examId);
}

