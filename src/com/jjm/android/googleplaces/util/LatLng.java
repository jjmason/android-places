package com.jjm.android.googleplaces.util;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.maps.GeoPoint;
import com.google.api.client.util.Key; 

/**
 * <p>A simple holder for a latitude and longitude.  Fields have
 * {@link Key} annotations, so it can be used with {@link ObjectParser}s.</p>
 * 
 * @author Jon Mason <jonathan.j.mason@gmail.com>
 * @version 1 
 */
public class LatLng {
	@Key("lat") private double mLatitude;
	@Key("lng") private double mLongitude;
	
	/**
	 * Construct a <code>LatLng</code> with integers as millidegrees.
	 * @param latitudeE6 The latitude * 1E6
	 * @param longitudeE6 The longitude * 1E6
	 */
	public LatLng(int latitudeE6, int longitudeE6){
		this((double)latitudeE6 / 1E6, (double) longitudeE6 / 1E6);
	}
	
	/**
	 * @param latitude The latitude in degrees
	 * @param longitude The longitude in degrees
	 */
	public LatLng(double latitude, double longitude){
		mLatitude = latitude;
		mLongitude = longitude;
	}
	
	/**
	 * The latitude of this point in degrees.
	 * @return the latitude in degrees
	 */
	public double getLatitude() {
		return mLatitude;
	}

	/**
	 * The longitude of this point in degrees.
	 * @return the longitude in degrees
	 */
	public double getLongitude() {
		return mLongitude;
	}
	
	/**
	 * The longitude of this point in millionths of a degree.
	 * @return the longitude * 1E6
	 */
	public int getLongitudeE6(){
		return (int)(1E6 * mLongitude); 
	}
	

	/**
	 * The latitude of this point in millionths of a degree.
	 * @return the latitude * 1E6
	 */
	public int getLatitudeE6(){
		return (int)(1E6 * mLatitude); 
	}
	
	/** 
	 * Convert this point to a {@link GeoPoint}.
	 * @return the geopoint
	 */
	public GeoPoint toGeoPoint(){
		return new GeoPoint(getLatitudeE6(), getLongitudeE6());
	}
	
	/**
	 * Helper to return the last known location.
	 * @param context Used to access location services.
	 * @return the last known location, or null if it is not available.
	 */
	public static LatLng lastKnown(Context context){
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		String provider = lm.getBestProvider(new Criteria(), true);
		if(provider != null){
			Location location = lm.getLastKnownLocation(provider);
			return new LatLng(location.getLatitude(), location.getLongitude());
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// XXX Several classes depend on toString returning this
		return mLatitude + "," + mLongitude;
	}
}
