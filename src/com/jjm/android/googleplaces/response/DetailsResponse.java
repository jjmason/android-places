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

import com.google.api.client.util.Key;
import com.jjm.android.googleplaces.Place;

/**
 * The result of a place details search.
 */
public class DetailsResponse extends PlacesResponse { 
	@Key("result")
	private Place mResult;

	/**
	 * The place details search result.
	 */
	public Place getResult() {
		return mResult;
	}
}
