/*
	This file is part of QuietPlaces.

    QuietPlaces is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    QuietPlaces is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with QuietPlaces.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jjm.android.googleplaces.response;

import java.util.Collections;
import java.util.List;

import com.google.api.client.util.Key;
import com.google.api.client.util.Value;

/**
 * Abstract base class for results from Google Places services.
 */ 
public abstract class PlacesResponse {
	@SuppressWarnings("serial")
	public static class BadStatusException extends RuntimeException {
		protected BadStatusException(Status status){
			super("Places service returned status \"" + status + "\"");
		}
	}
	public static enum Status {

		/**
		 * Indicates that no errors occurred; the place was successfully
		 * detected and at least one result was returned.
		 */
		@Value OK,
		/**
		 * Indicates that the search was successful but returned no results.
		 * This may occur if the search was passed a latlng in a remote
		 * location.
		 */
		@Value ZERO_RESULTS,
		/**
		 * Indicates that you are over your quota.
		 */
		@Value OVER_QUERY_LIMIT,
		/**
		 * Indicates that your request was denied, generally because of lack of
		 * a sensor parameter.
		 */
		@Value REQUEST_DENIED,
		/**
		 * Generally indicates that a required query parameter is missing.
		 */
		@Value INVALID_REQUEST, 
		/**
		 * Something went wrong on the server side.  Retrying may get a valid
		 * response.
		 */
		@Value UNKOWN_ERROR
		;
		/**
		 * <p>Throw a {@link BadStatusException} if status is not 
		 * equal to {@link #ZERO_RESULTS} or {@link #OK}. 
		 * @param passthru
		 * @return
		 */
		public void throwBadStatus(){
			if(this != OK && this != ZERO_RESULTS)
				throw new BadStatusException(this); 
		}
	}

	@Key("status")
	private Status mStatus;

	/**
	 * The status of the request.
	 */
	public final Status getStatus() {
		return mStatus;
	}
	
	@Key("html_attributions")
	private List<String> mHtmlAttributions;
	
	/**
	 * Contain a set of attributions about this listing which must be displayed
	 * to the user.  This field is never <code>null</code>.
	 */
	public final List<String> getHtmlAttributions(){
		return nullToEmpty(mHtmlAttributions);
	}
	 
	public PlacesResponse throwBadStatus(){
		getStatus().throwBadStatus();
		return this;
	}
	
	protected static <T> List<T> nullToEmpty(List<T> nullOrList){
		return nullOrList != null ? nullOrList : Collections.<T>emptyList();
	}
}
