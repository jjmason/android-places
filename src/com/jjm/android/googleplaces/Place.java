package com.jjm.android.googleplaces;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.jjm.android.googleplaces.util.LatLng;


/**
 * 
 * @author Jon Mason <jonathan.j.mason@gmail.com>
 * TODO Document Place
 */
public class Place extends GenericData implements HasReference { 
	public static class Geometry {
		@Key public LatLng location;
	}
	@Key("id") private String mId;
	@Key("name") private String mName;
	@Key("reference") private String mReference; 
	@Key("geometry") private Geometry mGeometry;
	@Key("formatted_address") private String mFormattedAddress; 
	@Key("vicinity") private String mVicinity;
	
	public String getId() {
		return mId;
	}
	
	public String getName() {
		return mName;
	}
	
	public String getReference() {
		return mReference;
	}
	
	public LatLng getLocation(){
		return mGeometry != null ? mGeometry.location : null;
	}
	
	public double getLatitude(){
		LatLng loc = getLocation();
		return loc != null ? loc.getLatitude() : Double.NaN;
	}
	
	public double getLongitude(){
		LatLng loc = getLocation();
		return loc != null ? loc.getLongitude() : Double.NaN;
	}
	
	public String getFormattedAddress() {
		return mFormattedAddress;
	}
	
	public String getVicinity() {
		return mVicinity;
	}
	
	
	
}
