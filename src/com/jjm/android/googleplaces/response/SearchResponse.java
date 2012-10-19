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

import java.util.List;

import com.google.api.client.util.Key;
import com.jjm.android.googleplaces.Place;

/**
 * A search result returned by the places search or text search service.
 */
public class SearchResponse extends PlacesResponse {
	@Key("next_page_token")
	private String mNextPageToken;

	/**
	 * This field contains a token that can be used to return up to 20
	 * additional results. It will be null if there are no additional results to
	 * display. The maximum number of results that can be returned is 60. There
	 * is a short delay between when a token is issued, and when it will become
	 * valid.
	 */
	public String getNextPageToken() {
		return mNextPageToken;
	}

	@Key("results")
	private List<Place> mResults;

	/**
	 * The search results. This field is never <code>null</code>.
	 */
	public List<Place> getResults() {
		return nullToEmpty(mResults);
	}

}
