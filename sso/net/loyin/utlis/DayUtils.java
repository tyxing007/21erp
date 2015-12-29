package net.loyin.utlis;

import java.util.Calendar;

/**
 * Created by EYKJ on 2015/9/25.
 */
public class DayUtils {

    /**
     * ȡ�õ�������
     */
    public static int getCurrentMonthLastDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);//����������Ϊ���µ�һ��
        a.roll(Calendar.DATE, -1);//���ڻع�һ�죬Ҳ�������һ��
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * �õ�ָ���µ�����
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//����������Ϊ���µ�һ��
        a.roll(Calendar.DATE, -1);//���ڻع�һ�죬Ҳ�������һ��
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

}
