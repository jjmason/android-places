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

import com.google.api.client.util.Key;
import com.jjm.android.googleplaces.response.SearchResponse;

/** 
 *TODO Document PlacesTextSearchRequest
 */
public class TextSearchRequest extends PlacesRequest<SearchResponse> {
	private static final String BASE_URL = 
			"https://maps.googleapis.com/maps/api/place/textsearch/json";
	
	@Key("query")
	private String mQuery;
	
	/* (non-Javadoc)
	 * @see com.rawrsoft.qp.places.PlacesRequest#getBaseUrl()
	 */
	@Override
	protected String getBaseUrl() {
		return BASE_URL;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return mQuery;
	}

	/**
	 * @param query the query to set
	 */
	public TextSearchRequest setQuery(String query) {
		mQuery = query;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.jjm.android.googleplaces.request.PlacesRequest#getResponseClass()
	 */
	@Override
	protected Class<SearchResponse> getResponseClass() {
		return SearchResponse.class;
	}

}
