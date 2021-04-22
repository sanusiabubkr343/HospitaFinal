package com.example.hospitalwaka.Models;

import java.util.List;

public class ResultsItem{
	private String reference;
	private List<String> types;
	private String icon;
	private String name;
	private OpeningHours openingHours;
	private Geometry geometry;
	private String vicinity;
	private List<PhotosItem> photos;
	private String placeId;

	public String getReference(){
		return reference;
	}

	public List<String> getTypes(){
		return types;
	}

	public String getIcon(){
		return icon;
	}

	public String getName(){
		return name;
	}

	public OpeningHours getOpeningHours(){
		return openingHours;
	}

	public Geometry getGeometry(){
		return geometry;
	}

	public String getVicinity(){
		return vicinity;
	}

	public List<PhotosItem> getPhotos(){
		return photos;
	}

	public String getPlaceId(){
		return placeId;
	}
}