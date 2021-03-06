package com.daniel.infomovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Daniel on 21/02/2017.
 */

public class MainActivity extends AppCompatActivity implements MoviesAdapter.Callback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(MovieItem item) {
        Log.e("MainActivity", "Item: " + item.title);

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movieItem", item);
        startActivity(intent);
    }
}
