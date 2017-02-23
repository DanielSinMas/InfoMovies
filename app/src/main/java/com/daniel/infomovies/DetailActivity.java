package com.daniel.infomovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static String TAG = DetailActivity.class.getSimpleName();
    private ImageView detailImageView;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        MovieItem movieItem = getIntent().getParcelableExtra("movieItem");
        Log.e(TAG, "Movie title: " +movieItem.title);

        detailImageView = (ImageView) findViewById(R.id.imageDetail);
        text = (TextView) findViewById(R.id.detailReleaseDate);
        text.setText(movieItem.release_date);
        text = (TextView) findViewById(R.id.detailOriginalTitle);
        text.setText(movieItem.title);
        text = (TextView) findViewById(R.id.detailVoteAverage);
        text.setText(movieItem.vote_average+" / 10");
        text = (TextView) findViewById(R.id.synopsis);
        text.setText(movieItem.synopsis);

        Picasso.with(this).load(Utility.getImageUrl()+movieItem.poster_path).into(detailImageView);


    }
}
