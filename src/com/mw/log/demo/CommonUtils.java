package com.mw.log.demo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CommonUtils {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final int INT = 30 * 60 * 1000;

    /**
     * 截取时间
     *
     * @param line
     * @throws ParseException
     */
    public static String subTime(String line) throws ParseException {
        int pointAt = line.indexOf("[", 0);
        String subTimeTemp = line.substring(pointAt + 1, line.length());
        String _subTime = subTimeTemp.replaceAll("May", "05");
        String subTime = convertTime(_subTime);
        return subTime;
    }

    /**
     * 转换时间
     *
     * @param tempSubTime
     * @return
     * @throws ParseException
     */
    public static String convertTime(String tempSubTime) throws ParseException {
        SimpleDateFormat convertFormat = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");
        Date date = convertFormat.parse(tempSubTime);// 有异常要捕获
        String newD = FORMAT.format(date);
        return newD;
    }

    /**
     * 返回结果
     *
     * @param subTimes
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("unused")
    public static List<LogResult> result(List<String> subTimes) throws ParseException {
        sortList(subTimes);
        List<LogResult> logResults = new ArrayList<LogResult>();
        LogResult logResult = null;
        String tempValue = subTimes.get(0);// 记录开始时间

        for (int i = 0; i < subTimes.size(); i++) {
            Date tempValueDate = FORMAT.parse(tempValue);
            String subTime_1 = subTimes.get(i);
            Date date1 = FORMAT.parse(subTime_1);
            if (i == subTimes.size() - 1) {// 防止数组角标越界
                if (date1.getTime() - tempValueDate.getTime() < INT) {
                    logResult = new LogResult();
                    logResult.setStartTime(tempValue);
                    logResult.setEndTime(subTime_1);
                } else {
                    logResult = new LogResult();
                    logResult.setStartTime(subTime_1);
                    logResult.setEndTime(subTime_1);
                }
                logResults.add(logResult);
                break;
            }
            String subTime_2 = subTimes.get(i + 1);
            Date date2 = FORMAT.parse(subTime_2);
            if ((date2.getTime() - date1.getTime()) < INT) {
                continue; // 还是一次访问,开始时间不变,继续寻找结束时间
            } else {
                // 说明这里有独立访问区分,结束时间是subTime_1,开始时间是tempValue,
                logResult = new LogResult();
                logResult.setStartTime(tempValue);
                tempValue = subTime_1;
                logResult.setEndTime(tempValue);
                tempValue = subTime_2; // 这个对象添加完成后,将tempValue设置成subTime_2,下一次独立访问的开始时间
            }
            logResults.add(logResult);
        }
        //处理毫秒
        logResults = getCount(logResults);
        return logResults;
    }

    /**
     * 对list进行排序
     *
     * @param subTimes
     */
    private static void sortList(List<String> subTimes) {
        Collections.sort(subTimes, new Comparator<String>() {
            public int compare(String o1, String o2) {
                Date d1 = null;
                Date d2 = null;
                try {
                    d1 = FORMAT.parse(o1);
                    d2 = FORMAT.parse(o2);
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    /**
     * 得到间隔的毫秒数
     *
     * @throws ParseException
     */
    public static List<LogResult> getCount(List<LogResult> logResults) throws ParseException {
        for (LogResult logResult : logResults) {
            String startTime = logResult.getStartTime();
            String endTime = logResult.getEndTime();
            long timeStart = FORMAT.parse(startTime).getTime();
            long timeEnd = FORMAT.parse(endTime).getTime();
            // 得到毫秒数
            long counts = timeEnd - timeStart;
            logResult.setStayLongTime(counts);
        }
        return logResults;

    }
}