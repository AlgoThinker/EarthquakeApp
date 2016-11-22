package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * Created by tusharsharma on 22/11/16.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<ListItemClass>> {

    String LOG_TAG  = EarthquakeLoader.class.getName();

    private String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super(context);

        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG,"strating the Async onStartLoading method .........");

        forceLoad();
    }

    @Override
    public List<ListItemClass> loadInBackground() {

        Log.v(LOG_TAG,"strating the Async loadInbackground method .........");
        List<ListItemClass> earthquakes = null;

        if (mUrl == null) {
            return null;
        }

        try {
            String responseStr =QueryUtils.makeHttpRequest(QueryUtils.createUrl(mUrl));

            earthquakes = QueryUtils.extractFeatureFromJson(responseStr);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return earthquakes;
    }
}
