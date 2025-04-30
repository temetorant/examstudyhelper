package com.example.examstudyhelper.ui.dailogs;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.examstudyhelper.R;

public class ExamRegistrationDialog extends DialogFragment {

    private OnExamRegisteredListener listener;

    //試験情報を受け取るためのコールバック
    public interface OnExamRegisteredListener {
        void onExamRegistered(String examName, String examDate);
    }

    public static ExamRegistrationDialog newInstance(OnExamRegisteredListener listener) {
        ExamRegistrationDialog dialog = new ExamRegistrationDialog();
        dialog.listener = listener;
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // ダイアログの幅を画面の 90% に設定
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            int height = WindowManager.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_exam_registration, container, false);

        EditText examNameEditText = view.findViewById(R.id.examNameEditText);
        EditText examYearEditText = view.findViewById(R.id.examYearEditText);
        EditText examMonthEditText = view.findViewById(R.id.examMonthEditText);
        EditText examDayEditText = view.findViewById(R.id.examDayEditText);
        Button btnRegisterButton = view.findViewById(R.id.btnRegisterButton); //登録
        Button btnCancelButton = view.findViewById(R.id.btnCancelButton); //キャンセル

        btnRegisterButton.setOnClickListener(v -> {
            String examName = examNameEditText.getText().toString().trim();
            String year = examYearEditText.getText().toString().trim();
            String month = examMonthEditText.getText().toString().trim();
            String day = examDayEditText.getText().toString().trim();

            if (TextUtils.isEmpty(examName) || TextUtils.isEmpty(year) || TextUtils.isEmpty(month) || TextUtils.isEmpty(day)) {
                Toast.makeText(getContext(), "試験名と日付を入力してください", Toast.LENGTH_SHORT).show();
                return;
            }

            // 年、月、日を結合して "YYYY年MM月DD日" の形式に変換
            String examDate = year + "年" + month + "月" + day + "日";

            if (listener != null) {
                listener.onExamRegistered(examName, examDate);
            }
            dismiss();
        });

        btnCancelButton.setOnClickListener(v -> dismiss());
        //ダイアログを閉じる
        return view;
    }
}


