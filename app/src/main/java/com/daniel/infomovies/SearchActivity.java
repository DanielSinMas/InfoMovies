package com.daniel.infomovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity implements OnTaskCompleted, MoviesAdapter.Callback{

    private Button searchButton;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private JSONArray array;
    private ArrayList<MovieItem> list;
    private MoviesAdapter moviesAdapter;
    private EditText editText;
    private TextView noResults;
    private static String TAG = SearchActivity.class.getSimpleName();
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchButton = (Button) findViewById(R.id.bt_search);
        recyclerView = (RecyclerView) findViewById(R.id.search_movie_recycler);
        progressBar = (ProgressBar) findViewById(R.id.pb_movies);
        editText = (EditText) findViewById(R.id.ed_search);
        noResults = (TextView) findViewById(R.id.tv_no_result);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noResults.setVisibility(View.GONE);
                if(editText.getText().toString()==null || editText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Cant search nothing", Toast.LENGTH_SHORT).show();
                }
                else{
                    editText.setEnabled(false);
                    recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), null));
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    addImages(editText.getText().toString());
                }
            }
        });

        Observable<String> stringObservable = createTextChangeObservable();
        disposable = stringObservable
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        //showProgressBar();
                        // TODO 1
                        // create showProgressBar function and hideProgressBar functions
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<String, ArrayList<MovieItem>>() {
                    @Override
                    public ArrayList<MovieItem> apply(String s) throws Exception {
                        //return addImages(s);
                        // TODO 2
                        // create interface to return values from asyncTask
                        return null;
                    }
                })
                .subscribe();
    }

    private void addImages(String searchText){
        SearchMovies get = new SearchMovies(this);
        get.execute(searchText);
    }

    @Override
    public void onTaskCompleted() {
        Log.e(TAG, "Array length: " +array.length());
        editText.setEnabled(true);
        if(array.length()<2){
            noResults.setVisibility(View.VISIBLE);
        }
        else{
            noResults.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            moviesAdapter = new MoviesAdapter(this, list);
            recyclerView.setAdapter(moviesAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            Log.e(TAG, "List size: " + list.size());
        }

    }

    private class SearchMovies extends AsyncTask<String, ArrayList<MovieItem>, JSONArray> {
        private OnTaskCompleted listener;

        public SearchMovies(OnTaskCompleted listener) {
            super();
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }


        protected JSONArray doInBackground(String...params) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(Utility.getSearchUrl(params[0]));
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
        protected void onPostExecute(JSONArray jsonArray) {
            progressBar.setVisibility(View.GONE);
            if(array!=null && array.length()>0) {
                list = new ArrayList<>();
                MovieItem movie_item;
                for (int i = 0; i<array.length(); i++) {
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
                        list.add(movie_item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            listener.onTaskCompleted();
        }
    }

    @Override
    public void onItemSelected(MovieItem item) {
        Log.e(TAG, "Item: " + item.title);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movieItem", item);
        startActivity(intent);
    }

    private Observable<String> createTextChangeObservable(){

        Observable<String> textChangeObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe( final ObservableEmitter<String> emitter) throws Exception {
                final TextWatcher watcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        emitter.onNext(charSequence.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                };

                editText.addTextChangedListener(watcher);

                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        editText.removeTextChangedListener(watcher);
                    }
                });

            }
        });

        return null;
    }
}
