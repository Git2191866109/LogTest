package com.mw.log.demo;

public class LogResult {
	private String startTime;// 起始请求时间
	private String endTime;// 结束请求时间
	private Long stayLongTime;// 毫秒

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Long getStayLongTime() {
		return stayLongTime;
	}

	public void setStayLongTime(Long stayLongTime) {
		this.stayLongTime = stayLongTime;
	}

	@Override
	public String toString() {
		return "LogResult{" +
				"startTime='" + startTime + '\'' +
				", endTime='" + endTime + '\'' +
				", stayLongTime=" + stayLongTime +
				'}';
	}
}
