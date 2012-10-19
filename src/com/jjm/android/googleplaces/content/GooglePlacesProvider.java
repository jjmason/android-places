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
package com.jjm.android.googleplaces.content;

import java.io.IOException;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.jjm.android.googleplaces.request.AutocompleteRequest;
import com.jjm.android.googleplaces.response.AutocompleteResponse;
import com.jjm.android.googleplaces.response.Prediction;
import com.jjm.android.googleplaces.util.Helpers;

/**
 * <p>
 * Content provider for google places. This class is mainly intended to support
 * search, and you're probably better off using the request classes directly.
 * </p>
 * 
 * <p>
 * This implementation will throw an exception if any write operations are
 * attempted.
 * </p>
 * 
 * <p>
 * Currently only supports autocomplete, with uris of this form:
 * <code>"content://com.jjm.android.googleplaces.content.GooglePlacesProvider/autocomplete/text"</code>
 * </p>
 * 
 * @author Jon Mason <jonathan.j.mason@gmail.com>
 */
public class GooglePlacesProvider extends ContentProvider {
	public static final String AUTHORITY = "com.jjm.android.googleplaces.content.GooglePlacesProvider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

	private static final int MATCH_AUTOCOMPLETE = 1;
	private static final UriMatcher sUriMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sUriMatcher.addURI(AUTHORITY, "autocomplete/*", MATCH_AUTOCOMPLETE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#query(android.net.Uri,
	 * java.lang.String[], java.lang.String, java.lang.String[],
	 * java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		if (sUriMatcher.match(uri) != MATCH_AUTOCOMPLETE)
			throw new IllegalArgumentException("invalid uri " + uri);
		// TODO Support location parameters
		String text = uri.getLastPathSegment();
		AutocompleteResponse response = null;
		try {
			response = new AutocompleteRequest()
					.setInput(text).useCurrentLocation(getContext())
					.execute(Helpers.createJsonRequestFactory());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} 
		
		response.getStatus().throwBadStatus();
		MatrixCursor cursor = new MatrixCursor(new String[]{
				BaseColumns._ID,
				SearchManager.SUGGEST_COLUMN_TEXT_1,
				SearchManager.SUGGEST_COLUMN_INTENT_DATA});
		long id = 0;
		for(Prediction prediction : response.getPredictions()){
			cursor.addRow(new Object[]{
				++ id,
				prediction.getDescription(),
				prediction.getReference()
			});
		}
		return cursor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case MATCH_AUTOCOMPLETE:
			return "vnd.com.android.dir/prediction";
		default:
			throw new IllegalArgumentException("invalid uri " + uri);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#insert(android.net.Uri,
	 * android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		throw new UnsupportedOperationException(
				"write operations are not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#delete(android.net.Uri,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException(
				"write operations are not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#update(android.net.Uri,
	 * android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		throw new UnsupportedOperationException(
				"write operations are not supported");
	}

}
