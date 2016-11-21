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

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

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

        new NetworkRequestAsyncTask().execute(USGS_REQUEST_URL);

    }


    private class NetworkRequestAsyncTask extends AsyncTask<String ,Void,List<ListItemClass>> {


        @Override
        protected List<ListItemClass> doInBackground(String... url) {

            List<ListItemClass> earthquakes = null;
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

            mAdapter.addAll(listItemClasses);


        }
    }
}
