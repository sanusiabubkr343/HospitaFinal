package com.example.hospitalwaka.Models;

import java.util.List;

public class PhotosItem{
	private String photoReference;
	private int width;
	private List<Object> htmlAttributions;
	private int height;

	public String getPhotoReference(){
		return photoReference;
	}

	public int getWidth(){
		return width;
	}

	public List<Object> getHtmlAttributions(){
		return htmlAttributions;
	}

	public int getHeight(){
		return height;
	}
}