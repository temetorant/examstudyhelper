package com.example.examstudyhelper.ui.dailogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.examstudyhelper.R;
import com.example.examstudyhelper.models.Task;

public class TaskDetailsDialog extends DialogFragment {

    private static final String ARG_TASK = "task";
    private Task task;

    public static TaskDetailsDialog newInstance(Task task) {
        TaskDetailsDialog dialog = new TaskDetailsDialog();
        Bundle args = new Bundle(); //Task オブジェクトを Bundle に格納
        args.putParcelable(ARG_TASK, task); // Parcelable に変更
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            task = getArguments().getParcelable(ARG_TASK, Task.class);  // Parcelable を取得
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_task_details, container, false);

        // UI要素の参照を取得
        TextView taskNameTextView = view.findViewById(R.id.tvTaskName);
        TextView taskCategoryTextView = view.findViewById(R.id.tvTaskCategory);
        TextView taskProgressTextView = view.findViewById(R.id.tvTaskProgress);
        TextView taskStatusTextView = view.findViewById(R.id.tvTaskStatus);

        // データをUIにセット
        if (task != null) {
            taskNameTextView.setText(task.getTaskName());
            taskCategoryTextView.setText(task.getCategory());
            taskProgressTextView.setText(getString(R.string.task_progress_format, task.getProgress())); // Stringリソースを使用
            taskStatusTextView.setText(task.isCompleted() ? getString(R.string.task_status_completed) : getString(R.string.task_status_incomplete));
        }

        // 閉じるボタン
        view.findViewById(R.id.btnClose).setOnClickListener(v -> dismiss());

        return view;
    }
    
    // ダイアログの幅を画面の 90% に設定
    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null && getDialog().getWindow() != null) {

            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            int height = WindowManager.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setLayout(width, height);
        }
    }
}

