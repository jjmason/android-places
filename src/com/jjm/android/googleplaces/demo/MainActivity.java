package com.jjm.android.googleplaces.demo;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;

import com.jjm.android.googleplaces.R;
import com.jjm.android.googleplaces.util.ApiKeys;
import com.jjm.android.googleplaces.util.GlobalDebug;
import com.jjm.android.googleplaces.view.PlacesAutocompleteController;

public class MainActivity extends Activity implements GlobalDebug{
	static {
		ApiKeys.addApiKey(ApiKeys.ID_GOOGLE_PLACES, "AIzaSyD3lY-r0ozuQCtMWpAcmaPwNzlvb4I0XOI");
	}
	
	private static final boolean DBG = GLOBAL_DEBUG;
	
    private static final String TAG = "MainActivity";

    private PlacesAutocompleteController mAutocompleter;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.demo_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        SearchManager manager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchableInfo info = manager.getSearchableInfo(getComponentName());
        
        if(DBG)
        	Log.d(TAG, "searchable=" + info);
        
        searchView.setSearchableInfo(info);
        searchView.setIconifiedByDefault(false);
        
        mAutocompleter = new PlacesAutocompleteController(searchView);
        return true;
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	if(mAutocompleter!=null)
    		mAutocompleter.setActive(false);
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
    	super.onResume();
    	if(mAutocompleter != null)
    		mAutocompleter.setActive(true);
    }
}
