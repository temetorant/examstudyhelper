package com.example.examstudyhelper.ui.dailogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.examstudyhelper.R;
import com.example.examstudyhelper.models.Task;

public class TaskRegistrationDialog extends DialogFragment {

    private OnTaskRegisteredListener listener;
    private int examId;

    public interface OnTaskRegisteredListener {
        void onTaskRegistered(Task task);
    }
    //試験IDを受け取り、DialogFragment に Bundle 経由で渡す
    public static TaskRegistrationDialog newInstance(int examId, OnTaskRegisteredListener listener) {
        TaskRegistrationDialog dialog = new TaskRegistrationDialog();
        dialog.listener = listener;
        Bundle args = new Bundle();
        args.putInt("examId", examId);
        dialog.setArguments(args);
        return dialog;
    }

    //examId保持
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            examId = getArguments().getInt("examId", -1);
        }
    }

    //表示サイズ９０％
    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            int height = WindowManager.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_task_registration, container, false);
        //UIセットアップ
        EditText etTaskName = view.findViewById(R.id.etTaskName);
        EditText etTargetTime = view.findViewById(R.id.etTargetTime);
        Spinner spinnerCategory = view.findViewById(R.id.spinnerCategory);
        ToggleButton switchComplete = view.findViewById(R.id.switchComplete);
        Button btnRegisterTask = view.findViewById(R.id.btnRegisterTask);
        Button btnCancelTask = view.findViewById(R.id.btnCancelTask);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.default_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        //登録ボタン処理
        btnRegisterTask.setOnClickListener(v -> {
            String taskName = etTaskName.getText().toString().trim();
            String targetTimeStr = etTargetTime.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();
            boolean isComplete = switchComplete.isChecked();

            if (taskName.isEmpty() || targetTimeStr.isEmpty()) {
                Toast.makeText(getContext(), "全ての入力が完了していません", Toast.LENGTH_SHORT).show();
                return;
            }

            int targetTime;
            try {
                targetTime = Integer.parseInt(targetTimeStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid time input.", Toast.LENGTH_SHORT).show();
                return;
            }

            //タスクオブジェクト
            Task task = new Task(taskName, targetTime, category, isComplete,0, examId);
            if (listener != null) {
                listener.onTaskRegistered(task);
            }
            dismiss();
        });

        btnCancelTask.setOnClickListener(v -> dismiss());

        return view;
    }
}




