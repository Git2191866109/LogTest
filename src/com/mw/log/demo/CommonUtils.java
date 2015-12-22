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
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");
		Date date = format.parse(tempSubTime);// 有异常要捕获
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newD = format.format(date);
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
	public static List<LogResult> result(List<String> subTimes)
			throws ParseException {
		sortList(subTimes);
		List<LogResult> logResults = new ArrayList<LogResult>();
		LogResult logResult = null;
		String tempValue = subTimes.get(0);// 记录最大值
		for (int i = 0; i < subTimes.size(); i++) {
			String subTime_1 = subTimes.get(i);
			if (i == subTimes.size() - 1) {// 防止数组角标越界
				break;
			}
			String subTime_2 = subTimes.get(i + 1);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date tempValueDate = sdf.parse(tempValue);
			Date date1 = sdf.parse(subTime_1);
			Date date2 = sdf.parse(subTime_2);
			if ((date2.getTime() - date1.getTime()) < 30 * 60 * 1000) {
				// 还是一次访问,开始时间不变,继续寻找结束时间
				continue;
			} else {
				// 说明这里有独立访问区分,结束时间是subTime_1,开始时间是tempValue,
				logResult = new LogResult();
				logResult.setStartTime(tempValue);
				tempValue = subTime_1;
				logResult.setEndTime(tempValue);
				// 这个对象添加完成后,将tempValue设置成subTime_2,这是下一次的
				tempValue = subTime_2;
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
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date d1 = null;
				Date d2 = null;
				try {
					d1 = df.parse(o1);
					d2 = df.parse(o2);
					return d1.compareTo(d2);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return 0;
			}
		});
	}

	/**
	 * 是否同一天
	 *
	 * @param subTime_1
	 * @param subTime_2
	 * @return
	 * @throws ParseException
	 */
	public static boolean isSameDay(String subTime_1, String subTime_2)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = sdf.parse(subTime_1);
		Date date2 = sdf.parse(subTime_2);
		// 判断两个日期之间的时间差与24*3600
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		boolean flag = (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
				&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1
				.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
		return flag;
	}

	/**
	 * 得到间隔的毫秒数
	 *
	 * @throws ParseException
	 */
	public static List<LogResult> getCount(List<LogResult> logResults) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (LogResult logResult : logResults) {
			String startTime = logResult.getStartTime();
			String endTime = logResult.getEndTime();
			long timeStart = sdf.parse(startTime).getTime();
			long timeEnd = sdf.parse(endTime).getTime();
			// 得到毫秒数
			long counts = timeEnd - timeStart;
			logResult.setStayLongTime(counts);
		}
		return logResults;

	}
}