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

import java.io.IOException;

import com.google.api.client.util.Key;
import com.jjm.android.googleplaces.HasReference;
import com.jjm.android.googleplaces.Place;
import com.jjm.android.googleplaces.response.DetailsResponse;
import com.jjm.android.googleplaces.response.Prediction;
import com.jjm.android.googleplaces.util.Helpers;

/**
 * <p>Represents a request to the place details service.</p>  
 * 
 * <p>The only meaningful field for this request type is {@link #getReference()}.</p>
 */
public class DetailsRequest extends PlacesRequest<DetailsResponse> {
	
	////////////////////////
	// PlacesRequest impl
	////////////////////////
	private static final String BASE_URL 
		= "https://maps.googleapis.com/maps/api/place/details/json";
	@Override
	protected String getBaseUrl() {
		return BASE_URL;
	}
	
	private static final Class<DetailsResponse> RESPONSE_CLASS =
			DetailsResponse.class;
	@Override
	protected Class<DetailsResponse> getResponseClass() {
		return RESPONSE_CLASS;
	}


	@Key("reference")
	private String mReference;

	/**
	 * The reference used to look up the place detail, as returned by
	 * {@link Place#getReference()}.
	 * 
	 * @return the reference
	 */
	public String getReference() {
		return mReference;
	}

	/**
	 * The reference used to look up the place detail, as returned by
	 * {@link Place#getReference()}.
	 * 
	 * @param reference
	 *            the reference
	 * @return this request
	 */
	public final DetailsRequest setReference(String reference) {
		mReference = reference;
		return this;
	}

	/**
	 * <p> Helper to get the details for a given reference.
	 * <p> Note that both {@link Place} and {@link Prediction}
	 * 	   implement the {@link HasReference} interface.
	 * <p> This shouldn't be called from a ui thread.
	 * @param reference a source for a reference
	 * @return the details
	 * @throws IOException
	 */
	public static Place get(HasReference reference) throws IOException {
		DetailsResponse resp = (DetailsResponse) of(reference)
				.execute(Helpers.createJsonRequestFactory())
				.throwBadStatus();
		return resp.getResult();
	}
	
	public static DetailsRequest of(HasReference ref){
		return new DetailsRequest().setReference(ref.getReference());
	}
}
