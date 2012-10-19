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

/**
 * @author Jon Mason <jonathan.j.mason@gmail.com>
 * TODO Document GooglePlacesSearchRequest
 */

import com.google.api.client.util.Key;
import com.google.api.client.util.Value;
import com.jjm.android.googleplaces.response.SearchResponse;

/**
 * TODO Document PlacesSearchRequest
 */
public class SearchRequest extends PlacesRequest<SearchResponse> {
	public static enum RankBy {
		@Value("prominence")
		Prominence, @Value("distance")
		Distance
	};

	private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/search/json";

	@Key("name")
	private String mName;
	@Key("keyword")
	private String mKeyword;
	@Key("rankby")
	private RankBy mRankBy;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rawrsoft.qp.places.PlacesRequest#getBaseUrl()
	 */
	@Override
	protected String getBaseUrl() {
		return BASE_URL;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public SearchRequest setName(String name) {
		mName = name;
		return this;
	}

	/**
	 * void
	 * 
	 * @return the keyword
	 */
	public String getKeyword() {
		return mKeyword;
	}

	/**
	 * @param keyword
	 *            the keyword to set
	 */
	public SearchRequest setKeyword(String keyword) {
		mKeyword = keyword;
		return this;
	}

	/**
	 * @return the rankBy
	 */
	public RankBy getRankBy() {
		return mRankBy;
	}

	/**
	 * @param rankBy
	 *            the rankBy to set
	 */
	public SearchRequest setRankBy(RankBy rankBy) {
		mRankBy = rankBy;
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
