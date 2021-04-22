package com.example.hospitalwaka.Models;

import java.util.List;

public class Model {
	private List<Object> htmlAttributions;
	private List<ResultsItem> results;
	private String status;

	public List<Object> getHtmlAttributions(){
		return htmlAttributions;
	}

	public List<ResultsItem> getResults(){
		return results;
	}

	public String getStatus(){
		return status;
	}

	@Override
	public String toString() {
		return "Model{" +
				"htmlAttributions=" + htmlAttributions +
				", results=" + results +
				", status='" + status + '\'' +
				'}';
	}
}