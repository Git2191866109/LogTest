package com.mw.log.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class LogTest {
	/**
	 * 读取文件
	 *
	 * @param readPath
	 *            文件路径
	 * @return
	 */
	public static List<String> read(String readPath) {
		List<String> subTimes = new ArrayList<String>();
		try {
			File f = new File(readPath);
			if (f.isFile() && f.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(f), "UTF-8");
				BufferedReader reader = new BufferedReader(read);
				String line = "";
				while ((line = reader.readLine()) != null) {
					String subTime = CommonUtils.subTime(line);
					subTimes.add(subTime);
				}
				read.close();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return subTimes;
	}

	public static void main(String[] args) throws ParseException {
		String readPath = "file/log.txt";
		System.out.println("=====================begin!!!===================================!");
		// 得到时间的集合
		List<String> subTimes = read(readPath);
		//返回处理的对象集合
		List<LogResult> reLogResults = CommonUtils.result(subTimes);
		for (LogResult logResult : reLogResults) {
			System.out.println(logResult.toString());
		}
		System.out.println("=====================end!!!===================================!");
	}
}
