package com.example.examstudyhelper.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");

    /**
     残り日数を計算するメソッド
      @param targetDate 試験日（yyyy年MM月dd日形式）
      @return 残り日数
     */
    public static int calculateDaysRemaining(String targetDate) {
        try {
            Date examDate = dateFormat.parse(targetDate);
            Date today = Calendar.getInstance().getTime();
            //今日の日付をdate型で取得

            long diffInMillis = examDate.getTime() - today.getTime();
            //取得した日にちと今日の日にちを計算
            // ミリ秒
            return (int) (diffInMillis / (1000 * 60 * 60 * 24));
            //60秒 × 60分 × 24時間 = 1日
            //小数点は切り捨て
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // エラーの場合
        }
    }

    /**
      今日の日付を文字列で取得するメソッド
      @return 今日の日付（yyyy年MM月dd日形式）
     */
    public static String getTodayDate() {
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

}
