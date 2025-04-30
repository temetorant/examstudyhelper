package com.example.examstudyhelper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examstudyhelper.models.Exam;
import com.example.examstudyhelper.R;

import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {

    private List<Exam> examList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Exam exam); // 通常クリック時
        void onExamLongClick(Exam exam); // 長押し時
    }

    public ExamAdapter(List<Exam> examList, OnItemClickListener listener) {
        this.examList = examList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exam, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        Exam currentExam = examList.get(position);
        holder.bind(currentExam, listener); // データとリスナーをバインド　Bindの所
    }

    @Override
    public int getItemCount() {
        return examList.size();
    }

    public static class ExamViewHolder extends RecyclerView.ViewHolder {
        private TextView examName;
        private TextView examDate;
        private TextView remainingDays;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            examName = itemView.findViewById(R.id.tvExamName);
            examDate = itemView.findViewById(R.id.tvExamDate);
            remainingDays = itemView.findViewById(R.id.tvDaysRemaining);
        }

        public void bind(Exam exam, OnItemClickListener listener) {
            examName.setText(exam.getExamName());
            examDate.setText(exam.getExamDate());
            remainingDays.setText(exam.getRemainingDays() + " 日後");

            // 通常クリック時のリスナー設定
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(exam);
                }
            });

            // 長押し時のリスナー設定
            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onExamLongClick(exam);
                }
                return true; // イベント消費
            });
        }
    }

    public void setExamList(List<Exam> newExamList) {
        this.examList = newExamList;
        notifyDataSetChanged();
    }
}
