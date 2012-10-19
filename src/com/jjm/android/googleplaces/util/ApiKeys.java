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
package com.jjm.android.googleplaces.util;

import java.util.HashMap;

import com.google.common.base.Preconditions;

/**
 * Container for API keys.  You should add your API keys from a static
 * initializer of a class that is always loaded, such as a main activity.
 */
public class ApiKeys {
	private static HashMap<String, String> sApiKeys =
			new HashMap<String, String>();
	
	public static final String ID_GOOGLE_PLACES = "googlePlaces";
	
	/**
	 * Add an api key.
	 * @param id an id that can be used to retreive the api key.
	 * @param apiKey the api key.
	 */
	public static void addApiKey(String id, String apiKey){
		sApiKeys.put(Preconditions.checkNotNull(id), 
				Preconditions.checkNotNull(apiKey));
	}
	
	/**
	 * Retreive an API key.
	 * @param id the id passed to {@link #addApiKey(String, String)}
	 * @return the api key, or null if it is not found.
	 */
	public static String getApiKey(String id){
		return sApiKeys.get(id);
	}
	
	/**
	 * Retrieve an API key.  If it has not been added, throw an 
	 * {@link ApiKeyNotFound} exception.
	 */
	public static String requireApiKey(String id){
		String key = getApiKey(id);
		if(key == null)
			throw new ApiKeyNotFound(id);
		return key;
	}
	
	@SuppressWarnings("serial")
	public static class ApiKeyNotFound extends RuntimeException {
		ApiKeyNotFound(String id) {
			super("No API key found for id \"" + id + "\"");
		}
	}
}
