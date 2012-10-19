/*
	This file is part of GooglePlaces.

    GooglePlaces is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    GooglePlaces is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with GooglePlaces.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jjm.android.googleplaces.request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.location.Location;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.common.base.Preconditions;
import com.jjm.android.googleplaces.HasGenericUrl;
import com.jjm.android.googleplaces.response.PlacesResponse;
import com.jjm.android.googleplaces.util.ApiKeys;
import com.jjm.android.googleplaces.util.LatLng;

/**
 * A request to the places service. Subclasses provide the base url and fields
 * annotated with {@link Key} to be used as query parameters.
 * 
 * @param T
 *            the type of response returned by the execute method on this
 *            request.
 */
public abstract class PlacesRequest<T extends PlacesResponse> extends
		GenericData implements HasGenericUrl {
	public static final double MAX_RADIUS = 5E5;
	@Key("key")
	private String mApiKey;
	@Key("sensor")
	private boolean mSensor;
	@Key("location")
	private LatLng mLocation;
	@Key("radius")
	private Double mRadius;
	@Key("language")
	private String mLanguage;

	private List<String> mTypes;

	public PlacesRequest() {
		mApiKey = getDefaultApiKey();
	}

	public PlacesRequest(String apiKey) {
		mApiKey = Preconditions.checkNotNull(apiKey);
	}

	/**
	 * Subclasses implement this method to provide service specific urls.
	 * 
	 * @return the base url
	 */
	protected abstract String getBaseUrl();

	/**
	 * Subclasses implement this to return the response class
	 */
	protected abstract Class<T> getResponseClass();

	protected String getDefaultApiKey() {
		return ApiKeys.requireApiKey(ApiKeys.ID_GOOGLE_PLACES);
	}

	protected void prepareData(Map<String, Object> data) {
		data.put("types", getTypesString());
	}

	private String getTypesString() {
		if (mTypes == null || mTypes.isEmpty())
			return null;
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (String s : mTypes) {
			if (!first)
				sb.append("|");
			first = false;
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * @param apiKey
	 *            the apiKey to set
	 */
	public PlacesRequest<T> setApiKey(String apiKey) {
		mApiKey = apiKey;
		return this;
	}

	/**
	 * @param sensor
	 *            the sensor to set
	 */
	public PlacesRequest<T> setSensor(boolean sensor) {
		mSensor = sensor;
		return this;
	}

	/**
	 * @param latLng
	 *            the location to set
	 */
	public PlacesRequest<T> setLocation(LatLng latLng) {
		mLocation = latLng;
		return this;
	}

	/**
	 * TODO Docs
	 * 
	 * @param location
	 * @return
	 */
	public PlacesRequest<T> setLocation(Location location) {
		LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
		return setLocation(l);
	}

	/**
	 * TODO Docs
	 * 
	 * @param lat
	 * @param lng
	 * @return
	 */
	public PlacesRequest<T> setLocation(double lat, double lng) {
		return setLocation(new LatLng(lat, lng));
	}

	/**
	 * @param radius
	 *            the radius to set
	 */
	public PlacesRequest<T> setRadius(Double radius) {
		// TODO: Check radius
		mRadius = radius;
		return this;
	}

	/**
	 * @param types
	 *            the types to set
	 */
	public PlacesRequest<T> setTypes(List<String> types) {
		mTypes = types;
		return this;
	}

	public List<String> getTypes() {
		if (mTypes == null) {
			mTypes = new ArrayList<String>();
		}
		return mTypes;
	}

	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return mApiKey;
	}

	/**
	 * @return the sensor
	 */
	public boolean isSensor() {
		return mSensor;
	}

	/**
	 * @return the location
	 */
	public LatLng getLocation() {
		return mLocation;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return mLanguage;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(String language) {
		mLanguage = language;
	}

	/**
	 * @return the radius
	 */
	public Double getRadius() {
		return mRadius;
	}

	public PlacesRequest<T> useCurrentLocation(Context context) {
		return setLocation(LatLng.lastKnown(context))
				.setRadius(MAX_RADIUS)
				.setSensor(true);
	}

	/**
	 * Execute the request
	 */
	public T execute(HttpRequestFactory requestFactory) throws IOException {
		return requestFactory.buildGetRequest(getGenericUrl()).execute()
				.parseAs(getResponseClass());
	}

	public GenericUrl getGenericUrl() {
		GenericUrl url = new GenericUrl(getBaseUrl());
		url.putAll(this);
		prepareData(url);
		return url;
	}
}
