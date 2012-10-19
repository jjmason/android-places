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

/**
 * Results from a places autocomplete service request.
 */
public class AutocompleteResponse extends PlacesResponse {
	@Key("predictions")
	private List<Prediction> mPredictions;

	/**
	 * Predictions for the provided text. This contains up to 5 items and is
	 * never <code>null</code>.
	 */
	public final List<Prediction> getPredictions() {
		return nullToEmpty(mPredictions);
	}

}
