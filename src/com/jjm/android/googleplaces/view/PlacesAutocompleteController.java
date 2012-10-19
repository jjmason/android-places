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
package com.jjm.android.googleplaces.view;

import java.util.List;

import android.app.Activity;
import android.app.SearchableInfo;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView.OnSuggestionListener;
import android.widget.TextView;

import com.jjm.android.googleplaces.request.AutocompleteRequest;
import com.jjm.android.googleplaces.request.PlacesRequest;
import com.jjm.android.googleplaces.response.AutocompleteResponse;
import com.jjm.android.googleplaces.response.Prediction;
import com.jjm.android.googleplaces.util.BackgroundTask;
import com.jjm.android.googleplaces.util.GlobalDebug;
import com.jjm.android.googleplaces.util.Helpers;

/**
 * <p>
 * Provides suggestions to a {@link SearchView} using the google places
 * autocomplete service.
 * </p>
 * <p>
 * To attach a {@link PlacesAutocompleteController} to a {@link SearchView}, you
 * pass the {@link SearchView} to the constructor
 * {@link #PlacesAutocompleteController(SearchView)}.
 * <p>
 * The owner of the {@link SearchView} should call {@link #setActive(boolean)}
 * to enable or disable suggestions when the search view becomes visible to or
 * hidden from the user. Typically an activity will do so in it's
 * {@link Activity#onPause()} and {@link Activity#onResume()} methods.
 * <p>
 * <b>Warning:</b> Nothing good will happen if you use this class with a
 * {@link SearchView} that has been configured using a {@link SearchableInfo}
 * object.
 * 
 */
public class PlacesAutocompleteController implements GlobalDebug {

	/**
	 * <p>
	 * Recieves notifications when the user interacts with the
	 * {@link SearchView}.
	 * </p>
	 */
	public interface OnSearchListener {
		/**
		 * Called when the user selects an item from the suggestions list.
		 * 
		 * @param prediction
		 *            The {@link Prediction} corresponding to the item selected.
		 */
		void onPredictionSelected(Prediction prediction);

		/**
		 * Called when the user submits a search that does not correspond to a
		 * selection from the suggestions list. This generally happens when the
		 * user enters text that does not correspond to a suggestion and then
		 * presses the search button.
		 * 
		 * @param text
		 *            The text returned by {@link SearchView#getQuery()}.
		 */
		void onQuerySubmitted(String text);
	}

	/**
	 * <p> Binds a view to the values contained in a prediction.</p>
	 * <p> Implementing this interface is just like extending 
	 * {@link CursorAdapter}.</p>
	 */
	public interface PredictionViewBinder {
		/**
		 * Create a new View to display a suggestion.
		 * @param context used for resources
		 * @param parent the parent of the view.
		 * @return the new view
		 * @see CursorAdapter#newView(Context, Cursor, ViewGroup)
		 */
		View newView(Context context, ViewGroup parent);

		/**
		 * Set the state of a view to correspond to the given prediction.
		 * @param context used for resources
		 * @param view the view to bind
		 * @param prediction the prediction
		 * @see CursorAdapter#bindView(View, Context, Cursor)
		 */
		void bindView(Context context, View view, Prediction prediction);
	}

	@SuppressWarnings("unused")
	private static final boolean DBG = GLOBAL_DEBUG;

	private static final String TAG = "Autocompleter";

	private final SearchView mSearchView;
	private final Context mContext;

	private String mSearchText;
	private boolean mSearching;
	private int mThreshold = 1;
	private boolean mActive = true;
	private List<Prediction> mPredictions;
	private OnSearchListener mOnSearchListener;
	private PredictionViewBinder mPredictionViewBinder;

	public PlacesAutocompleteController(Context context, SearchView searchView) {
		mContext = context;
		mSearchView = searchView;
		mSearchView.setOnQueryTextListener(mOnQueryTextListener);
		mSearchView.setOnSuggestionListener(mOnSuggestionListener);
		mSearching = false;
	}

	public PlacesAutocompleteController(SearchView searchView) {
		this(searchView.getContext(), searchView);
	}

	public boolean isActive() {
		return mActive;
	}

	public void setActive(boolean active) {
		mActive = active;
		if (mActive)
			maybeStartSearch();
	}

	public int getThreshold() {
		return mThreshold;
	}

	public void setThreshold(int threshold) {
		mThreshold = Math.max(1, threshold);
	}

	/**
	 * @param onSearchListener the onSearchListener to set
	 */
	public void setOnSearchListener(OnSearchListener onSearchListener) {
		mOnSearchListener = onSearchListener;
	}

	/**
	 * @param predictionViewBinder the predictionViewBinder to set
	 */
	public void setPredictionViewBinder(PredictionViewBinder predictionViewBinder) {
		mPredictionViewBinder = predictionViewBinder;
	}

	private final OnSuggestionListener mOnSuggestionListener = new OnSuggestionListener() {

		@Override
		public boolean onSuggestionSelect(int position) {
			if (mSearchView.isQueryRefinementEnabled()) {
				if (mPredictions != null && position < mPredictions.size()) {
					mSearchView.setQuery(
							getQueryForPrediction(mPredictions.get(position)),
							false);
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean onSuggestionClick(int position) {
			if (mPredictions != null && position < mPredictions.size()
					&& mOnSearchListener != null) {
				mOnSearchListener.onPredictionSelected(mPredictions
						.get(position));
			}
			return false; /* So that the suggestions list closes */
		}
	};

	private final OnQueryTextListener mOnQueryTextListener = new OnQueryTextListener() {

		@Override
		public boolean onQueryTextSubmit(String query) {
			// HACK: find a prediction whose text is generated as the text
			// in the search box.
			Prediction prediction = null;
			if (mPredictions != null) {
				for (Prediction p : mPredictions) {
					if (getQueryForPrediction(p).equals(query)) {
						prediction = p;
						break;
					}
				}
			}
			if (mOnSearchListener != null) {
				if (prediction != null) {
					mOnSearchListener.onPredictionSelected(prediction);
				} else {
					mOnSearchListener.onQuerySubmitted(query);
				}
			}
			return true;
		}

		@Override
		public boolean onQueryTextChange(String newText) {
			maybeStartSearch();
			return true;
		}
	};

	private String getQueryForPrediction(Prediction prediction) {
		return prediction.getDescription();
	}

	private Cursor createCursor() {
		// We include an _id column because cursor adapters require one.
		// We just store the index in mPredictions in this column.
		MatrixCursor c = new MatrixCursor(new String[] { "_id" });
		if (mPredictions != null) {
			for (int i = 0; i < mPredictions.size(); i++) {
				c.addRow(new Object[] { i });
			}
		}
		return c;
	}

	private CursorAdapter createAdapter() {
		return new PredictionsAdapter(createCursor());
	}

	private void maybeStartSearch() {
		if (!mActive || mSearching)
			return;
		String currentText = mSearchView.getQuery().toString();
		if (currentText.equals(mSearchText)
				|| currentText.length() < mThreshold)
			return;
		mSearchText = currentText;
		PlacesRequest<AutocompleteResponse> request = new AutocompleteRequest()
				.setInput(currentText).useCurrentLocation(mContext);
		// TODO Include offset -- I can't figure out how to get it from
		// the search view :(

		new SearchTask(request).execute();
	}

	private final PredictionViewBinder mDefaultPredictionViewBinder = new PredictionViewBinder() {

		@Override
		public View newView(Context context, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			return inflater.inflate(android.R.layout.simple_list_item_2, parent);
		}

		@Override
		public void bindView(Context context, View view, Prediction prediction) {
			TextView textView = (TextView) view.findViewById(android.R.id.text1);
			textView.setText(prediction.getAddressLine1());
			textView = (TextView) view.findViewById(android.R.id.text2);
			textView.setText(prediction.getAddressLine2());
		}
	};

	private PredictionViewBinder getPredictionViewBinder() {
		if (mPredictionViewBinder == null) {
			return mDefaultPredictionViewBinder;
		} else {
			return mPredictionViewBinder;
		}
	}

	private class PredictionsAdapter extends CursorAdapter {

		public PredictionsAdapter(Cursor c) {
			super(mContext, c, 0);
		}

		@Override
		public void bindView(View view, Context context, Cursor c) {
			getPredictionViewBinder().bindView(context, view,
					mPredictions.get(c.getInt(0)));
		}

		@Override
		public View newView(Context context, Cursor c, ViewGroup parent) {
			return getPredictionViewBinder().newView(context, parent);
		}
	}

	private class SearchTask extends BackgroundTask<AutocompleteResponse> {
		private final PlacesRequest<AutocompleteResponse> mRequest;

		public SearchTask(PlacesRequest<AutocompleteResponse> request) {
			mRequest = request;
		}

		@Override
		protected void onBefore() {
			mSearching = true;
		}

		@Override
		protected AutocompleteResponse call() throws Exception {
			return mRequest.execute(Helpers.createJsonRequestFactory());
		}

		@Override
		protected void onSuccess(AutocompleteResponse result) {
			mPredictions = result.getPredictions();
			mSearchView.setSuggestionsAdapter(createAdapter());
		}

		@Override
		protected void onException(Exception ex) {
			Log.e(TAG, "SearchTask threw exception", ex);
		}

		@Override
		protected void onFinally() {
			mSearching = false;
			maybeStartSearch();
		}
	}
}
