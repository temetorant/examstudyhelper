package com.example.examstudyhelper.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id; //主キー
    private String taskName;
    private int targetTime;
    private String category;
    private boolean isCompleted;
    private int progress; // 進捗（%）

    // 試験ID（このタスクがどの試験に関連するかを示す）
    private int examId;

    // デフォルトコンストラクタ（Roomのために必要）
    public Task() {
    }

    // カスタムコンストラクタ Roomに使わせない
    @Ignore
    public Task(String taskName, int targetTime, String category, boolean isCompleted, int progress, int examId) {
        this.taskName = taskName;
        this.targetTime = targetTime;
        this.category = category;
        this.isCompleted = isCompleted;
        this.progress = progress;
        this.examId = examId;
    }

    // Parcelable用のコンストラクタ
    protected Task(Parcel in) {
        id = in.readInt();
        taskName = in.readString();
        targetTime = in.readInt();
        category = in.readString();
        isCompleted = in.readByte() != 0;
        progress = in.readInt();
        examId = in.readInt(); // 追加
    }
    //Parcelable オブジェクトを復元するために使う。よく分からん
    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(taskName);
        dest.writeInt(targetTime);
        dest.writeString(category);
        dest.writeByte((byte) (isCompleted ? 1 : 0));
        dest.writeInt(progress);
        dest.writeInt(examId); // 追加
    }

    // ゲッターとセッター
    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getTaskName() {return taskName;}

    public void setTaskName(String taskName) {this.taskName = taskName;}

    public int getTargetTime() {return targetTime;}

    public void setTargetTime(int targetTime) {this.targetTime = targetTime;}

    public String getCategory() {return category;}

    public void setCategory(String category) {this.category = category;}

    public boolean isCompleted() {return isCompleted;}

    public void setCompleted(boolean completed) {isCompleted = completed;}

    public int getProgress() {return progress;}

    public void setProgress(int progress) {this.progress = progress;}

    // 試験IDのゲッター・セッター
    public int getExamId() {return examId;}

    public void setExamId(int examId) {this.examId = examId;}
}


