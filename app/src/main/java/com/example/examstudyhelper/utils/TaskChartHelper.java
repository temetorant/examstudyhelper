package com.example.examstudyhelper.utils;

import android.content.Context;
import androidx.core.content.ContextCompat;
import com.example.examstudyhelper.R;
import com.example.examstudyhelper.models.Task;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskChartHelper {
    private final BarChart barChart; //棒グラフ　
    private final Context context; //リソースアクセスや色の取得
    private final String[] categories;

    public TaskChartHelper(Context context, BarChart barChart) {
        this.context = context;
        this.barChart = barChart;
        this.categories = context.getResources().getStringArray(R.array.default_categories);
        setupBarChart();
    }

    private void setupBarChart() {
        barChart.getDescription().setEnabled(false); // 説明文（"Description Label"）を非表示
        barChart.getAxisRight().setEnabled(false);   // 右軸を非表示（左だけ使う）
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // X軸を下に表示
        barChart.getXAxis().setGranularity(1f);      // 間隔は1
        barChart.getXAxis().setDrawGridLines(false); // X軸のグリッド線を消す
        barChart.getAxisLeft().setGranularity(1f);
        barChart.getAxisLeft().setAxisMinimum(0f);   // Y軸の最小値を0%
        barChart.getAxisLeft().setAxisMaximum(100f); // 最大は100%（達成率）
    }

    //グラフの更新
    public void updateBarChart(List<Task> taskList) {
        int[] totalTasks = new int[categories.length];
        int[] completedTasks = new int[categories.length];
        //タスク件数と完了済み件数

        for (Task task : taskList) {
            for (int i = 0; i < categories.length; i++) {
                if (task.getCategory().equals(categories[i])) {
                    totalTasks[i]++;
                    if (task.isCompleted()) {
                        completedTasks[i]++;
                    }
                    break;
                }
            }
        }

        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < categories.length; i++) {
            float completionRate = totalTasks[i] > 0 ? (completedTasks[i] / (float) totalTasks[i]) * 100 : 0;
            entries.add(new BarEntry(i, completionRate));
            //達成率 = 完了タスク数 ÷ 総タスク数 × 100（％）
            //とりあえず普通の割合で
        }

        //色とサイズ
        BarDataSet dataSet = new BarDataSet(entries, context.getString(R.string.category_completion_rate));
        dataSet.setColor(ContextCompat.getColor(context, R.color.teal_700));
        dataSet.setValueTextSize(12f);

        //バーチャートにデータをセット
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(categories));
        xAxis.setLabelCount(categories.length);
        barChart.invalidate(); //描画の更新
    }
}
