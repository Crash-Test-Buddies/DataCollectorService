package edu.rit.se.datacollector.model;

import java.util.List;

public class StepTimerList {
	private List<StepTimer> timings;
	private Phone phone;

	public StepTimerList(){
	}
	
	public StepTimerList(List<StepTimer> timings){
		this.timings = timings;
	}
	
	public List<StepTimer> getTimings() {
		return timings;
	}

	public void setTimings(List<StepTimer> timings) {
		this.timings = timings;
	}

	public Phone getPhone() {
		return phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}
}
