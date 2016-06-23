package com.example.android.popularmusicapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GridActivityFragment extends Fragment {


    public GridActivityFragment() {

    }

    private static final String LOG_TAG = GridActivityFragment.class.getSimpleName();

    private final Utility mUtility = new Utility();

    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private View rootView;

    private static String CUR_PREF_ORDER;
    private SharedPreferences sharedpreferences;

    protected static String CUR_TITLE = null;
    protected static String CUR_RELEASE_DATE = null;
    protected static String CUR_POPULARITY = null;
    protected static String CUR_VOTE_AVE = null;
    protected static String CUR_SYNOPSIS = null;
    protected static Bitmap CUR_IMAGE = null;

    private AsyncMetadata jsonTask;

    private ProgressDialog mProgressDialog;
    private PowerManager.WakeLock mWakeLock;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        String tmp = sharedpreferences.getString(getString(R.string.pref_sort_order), getString(R.string.pref_most_popular));

        if (!tmp.equals(CUR_PREF_ORDER)) {

            CUR_PREF_ORDER = sharedpreferences.getString(getString(R.string.pref_sort_order), getString(R.string.pref_most_popular));
            processSearchText();
        } else {
            Log.d(LOG_TAG, "query String is null or empty.");
        }

    }


    private void processSearchText() {

        jsonTask = new AsyncMetadata();
        jsonTask.execute();

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                jsonTask.cancel(true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        gridView = (GridView) rootView.findViewById(R.id.GridLayout_Movie);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                CUR_TITLE = item.getTitle();
                CUR_POPULARITY = item.getPopularity();
                CUR_SYNOPSIS = item.getSynopsis();
                CUR_VOTE_AVE = item.getVoteAve();
                CUR_IMAGE = item.getImage();
                CUR_RELEASE_DATE = item.getReleaseDate();

                Intent detailsIntent = new Intent(getActivity().getApplicationContext(), MovieDetails.class);

                startActivity(detailsIntent);

            }
        });

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.on_progress_caption));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);


        return rootView;
    }


    private class AsyncMetadata extends AsyncTask<Void, Integer, List<Map<String, String>>> {

        private final String LOG_TAG = GridActivity.class.getSimpleName();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            PowerManager pm = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(10);
            mProgressDialog.setProgress(progress[0]);

        }


        @Override
        protected void onPostExecute(List<Map<String, String>> list) {

            if(list!=null && list.size()>0) {

                final AsyncImage imageTask = new AsyncImage() {
                    @Override
                    protected void onPostExecute(ArrayList<ImageItem> imageItems) {
                        if (imageItems.size() > 0) {

                            gridAdapter = new GridViewAdapter(getActivity().getApplicationContext(), R.layout.grid_item_layout, imageItems);
                            gridView.setAdapter(gridAdapter);

                        } else {
                            gridAdapter = new GridViewAdapter(getActivity().getApplicationContext(), R.layout.grid_item_layout, new ArrayList());
                            gridView.setAdapter(gridAdapter);
                        }

                    }
                };
                imageTask.execute(list);
            }
            else {
                mWakeLock.release();
                mProgressDialog.dismiss();

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {

                            Toast toast = Toast.makeText(getContext(), "No movies for that.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 100);
                            toast.show();
                        }
                    });
                    Log.d(LOG_TAG, "No Movies for that.");
            }
        }

        private String getResponseForUri(String uri) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(uri);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {

                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }
                return buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error by IO ", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return "";
        }

        @Override
        protected List<Map<String, String>> doInBackground(Void ... params) {

            List<Map<String, String>> jsonList = new ArrayList<>();

            try {
                    String tmp = sharedpreferences.getString(getString(R.string.pref_sort_order), getString(R.string.pref_most_popular));

                    String pref_uri = mUtility.getUriForPref(getActivity(), tmp, getString(R.string.pref_most_popular_default), getString(R.string.pref_vote_ave_default));
                    String response = getResponseForUri(pref_uri);

                    Log.d("DEBUGINGATTENTION: ", pref_uri);

                    jsonList = mUtility.formatJsonResponse(response);

                return jsonList;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "JSON Error", e);
            }
            return jsonList;
        }
    }

        private class AsyncImage extends AsyncTask<List<Map<String, String>>, Integer, ArrayList<ImageItem>> {

            private final String LOG_TAG = AsyncImage.class.getSimpleName();

            /** inspired by Developing Android Apps - Udacity course **/
            public Bitmap loadBitmap(String posterPath) {

                String uri = mUtility.getUriStringForImages(getActivity(), posterPath);

                Bitmap bm = null;
                InputStream is = null;
                BufferedInputStream bis = null;

                try {
                    URLConnection conn = new URL(uri).openConnection();
                    conn.connect();
                    is = conn.getInputStream();
                    bis = new BufferedInputStream(is, 8192);
                    bm = BitmapFactory.decodeStream(bis);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return bm;
            }


            @Override
            protected ArrayList<ImageItem> doInBackground(List<Map<String, String>>... params) {

                List<Map<String, String>> list = params[0];

                try {

                    return publishImageBulk(list);

                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
                return null;
            }

            private ArrayList<ImageItem> publishImageBulk(List<Map<String, String>> helperList) {
                ArrayList<ImageItem> bmps = new ArrayList<>();

                Bitmap bm = null;

                for (int i = 0; i < helperList.size(); i++) {
                    Map cur = helperList.get(i);
                    String posterPath = cur.get(Constants.POSTER_PATH).toString().substring(1);
                    if (posterPath != "") {
                        bm = loadBitmap(posterPath);
                    }
                    if (bm == null) {
                        bm = BitmapFactory.decodeResource(getResources(), R.drawable.not_available);
                    }

                    bmps.add(new ImageItem(bm, cur.get(Constants.OVERVIEW).toString(),
                            cur.get(Constants.VOTE_AVE).toString(),
                            cur.get(Constants.POPULARITY).toString(),
                            cur.get(Constants.TITLE).toString(),
                            cur.get(Constants.RELEASE_DATE).toString()));

                }
                mWakeLock.release();
                mProgressDialog.dismiss();
                return bmps;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                jsonTask.onProgressUpdate(values);
            }
        }

}