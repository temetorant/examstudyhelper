package com.example.examstudyhelper.utils;

public class ProgressCalculator {

    /**
      全体の進捗率を計算するメソッド
      @param totalTargetTime 合計目標時間
      @param completedTime 完了した勉強時間
      @return 進捗率（0.0～100.0）
     */
    //javadocって使い方これで合ってる？
    public static double calculateOverallProgress(int totalTargetTime, int completedTime) {
        if (totalTargetTime <= 0) {
            return 0.0;
            //基本は０％　１００以上は１００％固定
        }
        return Math.min((completedTime / (double) totalTargetTime) * 100, 100.0);
    }

    /**
     * 1日あたりの目標勉強時間を計算するメソッド
    * @param remainingDays 残り日数
    * @param remainingTargetTime 残り目標時間
    * @return 1日あたりの目標勉強時間
     */
    public static double calculateDailyStudyTime(int remainingDays, int remainingTargetTime) {
        if (remainingDays <= 0) {
            return remainingTargetTime;
            // 残り日数がない場合は全ての時間を今日に割り当てる
            //要改修　試験日が過ぎた試験は隔離するとか？
        }
        return remainingTargetTime / (double) remainingDays;
    }
}
