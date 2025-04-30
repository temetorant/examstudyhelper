package com.example.examstudyhelper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examstudyhelper.R;
import com.example.examstudyhelper.models.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private OnTaskToggleListener toggleListener;
    private OnTaskDeleteListener deleteListener;
    private OnTaskClickListener clickListener;

    // 完了状態変更リスナ
    public interface OnTaskToggleListener {
        void onTaskToggle(Task task, boolean isComplete);
    }

    // 削除リスナー
    public interface OnTaskDeleteListener {
        void onTaskDelete(Task task);
    }

    // 新しいクリックリスナー
    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    // コンストラクタを修正
    public TaskAdapter(List<Task> taskList, OnTaskToggleListener toggleListener, OnTaskDeleteListener deleteListener, OnTaskClickListener clickListener) {
        this.taskList = taskList;
        this.toggleListener = toggleListener;
        this.deleteListener = deleteListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = taskList.get(position);
        holder.taskName.setText(currentTask.getTaskName());
        holder.targetTime.setText(currentTask.getTargetTime() + " 時間");
        holder.category.setText(currentTask.getCategory());

        // リスナーを一旦解除してから、ボタンの状態を設定
        holder.toggleButton.setOnCheckedChangeListener(null);
        holder.toggleButton.setChecked(currentTask.isCompleted());

        // 新しくリスナーを設定
        holder.toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (toggleListener != null) {
                toggleListener.onTaskToggle(currentTask, isChecked);
            }
            currentTask.setCompleted(isChecked);
        });

        // 削除ボタンのクリックリスナー
        holder.deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onTaskDelete(currentTask);
            }
        });

        // タスク項目全体のクリックリスナー
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onTaskClick(currentTask);
            }
        });
    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView taskName;
        private TextView targetTime;
        private TextView category;
        private ToggleButton toggleButton;
        private ImageButton deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.tvTaskName);
            targetTime = itemView.findViewById(R.id.tvTaskTargetTime);
            category = itemView.findViewById(R.id.tvTaskCategory);
            toggleButton = itemView.findViewById(R.id.switchTaskStatus);
            deleteButton = itemView.findViewById(R.id.btnDeleteTask);
        }
    }

    public void setTaskList(List<Task> newTaskList) {
        this.taskList = newTaskList;
        notifyDataSetChanged();
    }
}



