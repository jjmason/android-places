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
package com.jjm.android.googleplaces.request;

import android.content.Context;

import com.google.api.client.util.Key;
import com.jjm.android.googleplaces.response.AutocompleteResponse;

/**
 * TODO Document PlacesAutocompleteRequest
 */
public class AutocompleteRequest extends PlacesRequest<AutocompleteResponse> {

	private static final String BASE_URL = 
			"https://maps.googleapis.com/maps/api/place/autocomplete/json";

	@Override
	protected String getBaseUrl() {
		return BASE_URL;
	}

	private static final Class<AutocompleteResponse> RESPONSE_CLASS = 
			AutocompleteResponse.class;

	@Override
	protected Class<AutocompleteResponse> getResponseClass() {
		return RESPONSE_CLASS;
	}

	@Key("input")
	private String mInput;
	@Key("offset")
	private Integer mOffset;

	/**
	 * @return the input
	 */
	public String getInput() {
		return mInput;
	}

	/**
	 * @param input
	 *            the input to set
	 */
	public AutocompleteRequest setInput(String input) {
		mInput = input;
		return this;
	}

	/**
	 * @return the offset
	 */
	public Integer getOffset() {
		return mOffset;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public AutocompleteRequest setOffset(Integer offset) {
		mOffset = offset;
		return this;
	}

	public static AutocompleteRequest of(Context locationSource, String input) {
		return (AutocompleteRequest) 
				new AutocompleteRequest()
				.setInput(input)
				.useCurrentLocation(locationSource);
	}

}
