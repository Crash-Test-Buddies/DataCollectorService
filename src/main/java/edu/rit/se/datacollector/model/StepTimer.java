package edu.rit.se.datacollector.model;

import java.sql.Timestamp;
import java.util.List;

public class StepTimer {
	List<StepTimer> timings;
	private String stepName;
	private Timestamp startTime;
	private Timestamp endTime;
	private double latitude;
	private double longitude;
	private long startSeconds;
	private long endSeconds;
	
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	
	public long getStartSeconds() {
		return startSeconds;
	}
	
	public void setStartTime(String startTime) {
		long milliseconds = Long.parseLong(startTime);
		// Convert milliseconds to seconds
		this.startSeconds = milliseconds * 1000;
		this.startTime = new Timestamp(milliseconds);
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	
	public long getEndSeconds() {
		return endSeconds;
	}
	public void setEndTime(String endTime) {
		long milliseconds = Long.parseLong(endTime);
		// Convert to seconds
		this.endSeconds = milliseconds * 1000;
		this.endTime = new Timestamp(milliseconds);
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = Double.parseDouble(latitude);
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = Double.parseDouble(longitude);
	}
	public List<StepTimer> getTimings() {
		return timings;
	}
	public void setTimings(List<StepTimer> timings) {
		this.timings = timings;
	}
}
