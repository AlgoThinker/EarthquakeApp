/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<ListItemClass>>{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    public static final int EARTHQUAKE_LOADER_ID = 1;

    private static final String USGS_REQUEST_URL =
            "http://earthquake.usgs.gov/fdsnws/event/1/query";


    ListItemAdapter mAdapter = null;

    TextView emptyStateTextView ;

    ProgressBar mProgressBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        mProgressBar = (ProgressBar)findViewById(R.id.progres_bar);

        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new ListItemAdapter(this , new ArrayList<ListItemClass>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        emptyStateTextView = (TextView) findViewById(R.id.empty_state);

        //This method is provided by SDK and executes only when then is not item in listview(no results returned)

        earthquakeListView.setEmptyView(emptyStateTextView);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            initiateDataFetchTask();
        } else {
            mProgressBar.setVisibility(View.GONE);
            emptyStateTextView.setText("No internet connection");
        }

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               ListItemClass currentQuake =  mAdapter.getItem(position);

                Uri earthquakeUri = Uri.parse(currentQuake.getUrl());


                Intent siteIntent = new Intent(Intent.ACTION_VIEW,earthquakeUri);

                startActivity(siteIntent);


            }
        });


    }


    private void initiateDataFetchTask() {

        android.app.LoaderManager loaderManager = getLoaderManager();

        Log.v(LOG_TAG, "starting the initLoader .......");
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

//        Log.v(LOG_TAG,"starting the asynctask .......");
//        new NetworkRequestAsyncTask().execute(USGS_REQUEST_URL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<ListItemClass>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG,"strating the loader onCreate method .........");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPreferences.getString(getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("limit","10");
        uriBuilder.appendQueryParameter("minmag",minMagnitude);
        uriBuilder.appendQueryParameter("orderby",orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<ListItemClass>> loader, List<ListItemClass> listItemClasses) {

        Log.v(LOG_TAG,"starting the loader onLoaderFinished method .........");

        mAdapter.clear();
        if (listItemClasses != null && !listItemClasses.isEmpty()) {
            mAdapter.addAll(listItemClasses);
        }

        emptyStateTextView.setText(R.string.NoEarthquakesFound);

        mProgressBar.setVisibility(View.GONE);


    }

    @Override
    public void onLoaderReset(Loader<List<ListItemClass>> loader) {

        Log.v(LOG_TAG,"starting the loader onLoaderReset method .........");


        mAdapter.clear();
    }


   /*private class NetworkRequestAsyncTask extends AsyncTask<String ,Void,List<ListItemClass>> {


        @Override
        protected List<ListItemClass> doInBackground(String... url) {

            Log.v(LOG_TAG,"strating the doinbackground method .........");

            List<ListItemClass> earthquakes = null;

            if (url.length < 1 || url[0] == null) {
                return null;
            }

            try {
                String responseStr =QueryUtils.makeHttpRequest(QueryUtils.createUrl(url[0]));

                earthquakes = QueryUtils.extractFeatureFromJson(responseStr);

            } catch (IOException e) {
                e.printStackTrace();
            }


            return earthquakes;
        }

        @Override
        protected void onPostExecute(List<ListItemClass> listItemClasses) {
            super.onPostExecute(listItemClasses);
            Log.v(LOG_TAG,"strating the onPostexecute method .........");
            mAdapter.clear();
            if (listItemClasses != null && !listItemClasses.isEmpty()) {
                mAdapter.addAll(listItemClasses);
            }


        }
    }*/
}
