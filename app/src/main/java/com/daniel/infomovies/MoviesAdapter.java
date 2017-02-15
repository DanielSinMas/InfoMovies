package com.daniel.infomovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Daniel on 15/02/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    public static final String TAG = MoviesAdapter.class.getSimpleName();
    private int numberItems;

    public MoviesAdapter(int numberItems){
        this.numberItems = numberItems;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MoviesViewHolder viewHolder = new MoviesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
    }

    @Override
    public int getItemCount() {
        return numberItems;
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder{

        ImageView movieImage;
        TextView movieTitle;

        public MoviesViewHolder(View itemView) {
            super(itemView);

            movieImage = (ImageView) itemView.findViewById(R.id.iv_movie_thumbnail);
            movieTitle = (TextView) itemView.findViewById(R.id.tv_movie_title);
        }
    }
}
