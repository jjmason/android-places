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

import android.util.Log;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.jjm.android.googleplaces.HasReference;

/**
 * An item returned by a places autocomplete request.
 */
public class Prediction extends GenericData implements HasReference {
	/**
	 * A matched section of the description. A section in the description is
	 * typically terminated by a comma.
	 */
	public static class Term {
		@Key("value")
		private String mValue;
		@Key("offset")
		private int mOffset;

		/**
		 * @return the text of the term.
		 */
		public String getValue() {
			return mValue;
		}

		/**
		 * @return the offset of the term in the description
		 */
		public int getOffset() {
			return mOffset;
		}

		public String toString() {
			return "[" + getValue() + "@" + getOffset() + "]";
		}
	}

	/**
	 * {@hide}
	 */
	public static class MatchedSubstring {
		@Key("offset")
		public int offset;
		@Key("length")
		public int length;
	}

	private static final String TAG = "Prediction";

	@Key("description")
	private String mDescription;
	@Key("reference")
	private String mReference;
	@Key("id")
	private String mId;
	@Key("terms")
	private List<Term> mTerms;
	@Key("matched_substring")
	private MatchedSubstring mMatchedSubstring;

	/**
	 * The human-readable name for the returned result. For establishment
	 * results, this is usually the business name.
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * A unique token that you can use to retrieve additional information about
	 * this place in a Place Details request. You can store this token and use
	 * it at any time in future to refresh cached data about this Place, but the
	 * same token is not guaranteed to be returned for any given Place across
	 * different searches.
	 */
	public String getReference() {
		return mReference;
	}

	/**
	 * A unique stable identifier denoting this place. This identifier may not
	 * be used to retrieve information about this place, but can be used to
	 * consolidate data about this Place, and to verify the identity of a Place
	 * across separate searches.
	 */
	public String getId() {
		return mId;
	}

	/**
	 * An array of terms identifying each section of the returned description (a
	 * section of the description is generally terminated with a comma). This is
	 * never <code>null<code>
	 */
	public List<Term> getTerms() {
		return mTerms == null ? Collections.<Term> emptyList() : mTerms;
	}

	/**
	 * The offset of the predicted term in the search text, or -1 if this is not
	 * available. Can be used for highlighting.
	 */
	public int getOffset() {
		return mMatchedSubstring != null ? mMatchedSubstring.offset : -1;
	}

	/**
	 * The length of the predicted term in the search text, or -1 if this is not
	 * available. Can be used for highlighting.
	 */
	public int getLength() {
		return mMatchedSubstring != null ? mMatchedSubstring.length : -1;
	}

	/**
	 * Generate the first line of the address.  This is based on the terms,
	 * and is approximate.
	 */
	public String getAddressLine1(){
		List<Term> terms = getTerms();
		if(terms == null){
			Log.wtf(TAG, "terms was null??");
			return getDescription();
		}
		if(terms.isEmpty()){
			Log.wtf(TAG, "terms was empty??");
			return getDescription();
		}
		StringBuilder sb = new StringBuilder(terms.get(0).getValue());
		if(terms.size() > 1){
			sb.append(",").append(terms.get(1).getValue());
		}
		return sb.toString();
	}
	
	/**
	 * Generate the second line of the address.  This is based on terms, 
	 * and is approximate.
	 */
	public String getAddressLine2(){
		List<Term> terms = getTerms();
		if(terms == null){
			Log.wtf(TAG, "terms was null??");
			return getDescription();
		}
		if(terms.size() < 3){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(int i=2;i<terms.size();i++){
			if(i != 2){
				sb.append(",");
			}
			sb.append(terms.get(i).getValue());
		}
		return sb.toString();
	}
	
	public String toString() {
		return "Prediction[description=\"" + getDescription() + "\",offset="
				+ getOffset() + ",length=" + getLength() + ",terms="
				+ getTerms() + ",id=" + getId() + ",reference="
				+ getReference() + "]";
	}
}
