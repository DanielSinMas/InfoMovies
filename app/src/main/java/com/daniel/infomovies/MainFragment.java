package com.daniel.infomovies;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class MainFragment extends Fragment implements OnTaskCompleted, MoviesAdapter.Callback{

    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;
    private JSONArray array;
    private MovieItem[] list;
    private ProgressDialog progressDialog;
    private String TAG = MainFragment.class.getSimpleName();

    public MainFragment() {}

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment, menu);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler);

        if(savedInstanceState != null){
            list=(MovieItem[])savedInstanceState.get("arrayList");
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        moviesAdapter = new MoviesAdapter(getActivity(), null);
        recyclerView.setAdapter(moviesAdapter);
        addImages();
    }

    private void addImages(){
        progressDialog = new ProgressDialog(getActivity());
        getMovies get = new getMovies(this, progressDialog);
        get.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleted() {
        moviesAdapter = new MoviesAdapter(getActivity(), Arrays.asList(list));
        recyclerView.setAdapter(moviesAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        Log.e(TAG, "List size: " + list.length);
    }

    @Override
    public void onItemSelected(MovieItem item) {

    }

    private class getMovies extends AsyncTask{
        private OnTaskCompleted listener;
        private ProgressDialog progressDialog;

        public getMovies(OnTaskCompleted listener, ProgressDialog progress) {
            super();
            this.progressDialog=progress;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Loading");
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(Utility.getDiscoverUrl());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
                String line;
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                String s = response.toString();
                JSONObject obj = new org.json.JSONObject(s);
                array = obj.getJSONArray("results");
                return array;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();
            if(array!=null && array.length()>0) {
                list = new MovieItem[10];
                MovieItem movie_item;
                for (int i = 0; i < 10; i++) {
                    try {
                        JSONObject obj = (JSONObject) array.get(i);
                        movie_item = new MovieItem(
                                obj.get("id").toString(),
                                obj.get("original_title").toString(),
                                obj.get("poster_path").toString(),
                                obj.get("overview").toString(),
                                Float.parseFloat(obj.get("vote_average").toString()),
                                obj.get("release_date").toString()
                        );
                        list[i] = movie_item;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listener.onTaskCompleted();
            }
        }
    }
}
