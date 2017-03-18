package com.zgmz.ls.model;

public class District extends BaseData {
	
	private String provice;
	
	private String city;
	
	private String distric;
	
	private String address;
	
	private String location;
	
	private double latitude;
	
	private double longitude;

	public String getProvice() {
		return provice;
	}

	public void setProvice(String provice) {
		this.provice = provice;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistric() {
		return distric;
	}

	public void setDistric(String distric) {
		this.distric = distric;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "District [userId=" + getUserId() + ", provice=" + provice + ", city=" + city + ", distric=" + distric
				+ ", address=" + address + ", location=" + location + ", latitude=" + latitude + ", longitude="
				+ longitude + "]";
	}

	
}
