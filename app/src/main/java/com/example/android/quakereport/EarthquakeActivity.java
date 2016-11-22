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
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<ListItemClass>>{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    public static final int EARTHQUAKE_LOADER_ID = 1;

    private static final String USGS_REQUEST_URL =
            "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=2&limit=100";


    ListItemAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new ListItemAdapter(this , new ArrayList<ListItemClass>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               ListItemClass currentQuake =  mAdapter.getItem(position);

                Uri earthquakeUri = Uri.parse(currentQuake.getUrl());


                Intent siteIntent = new Intent(Intent.ACTION_VIEW,earthquakeUri);

                startActivity(siteIntent);


            }
        });

        android.app.LoaderManager loaderManager = getLoaderManager();

        Log.v(LOG_TAG,"starting the initLoader .......");
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);

//        Log.v(LOG_TAG,"starting the asynctask .......");
//        new NetworkRequestAsyncTask().execute(USGS_REQUEST_URL);

    }

    @Override
    public Loader<List<ListItemClass>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG,"strating the loader onCreate method .........");
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<ListItemClass>> loader, List<ListItemClass> listItemClasses) {

        Log.v(LOG_TAG,"starting the loader onLoaderFinished method .........");

        mAdapter.clear();
        if (listItemClasses != null && !listItemClasses.isEmpty()) {
            mAdapter.addAll(listItemClasses);
        }

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
