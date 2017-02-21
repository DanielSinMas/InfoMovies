package com.daniel.infomovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Daniel on 15/02/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<MovieItem> list;
    public static final String TAG = MoviesAdapter.class.getSimpleName();
    private int numberItems;
    private Context context;

    public interface Callback{
        void onItemSelected(MovieItem item);
    }

    public MoviesAdapter(Context context, List<MovieItem> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        view.setMinimumHeight(parent.getMeasuredHeight() / 2);
        MoviesViewHolder viewHolder = new MoviesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, final int position) {
        Picasso.with(context).load(Utility.getImageUrl()+list.get(position).poster_path).into(holder.movieImage);

        holder.movieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Callback) context).onItemSelected(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list!=null) return list.size();
        else return 0;
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder{

        ImageView movieImage;

        public MoviesViewHolder(View itemView) {
            super(itemView);

            movieImage = (ImageView) itemView.findViewById(R.id.iv_movie_thumbnail);
        }
    }
}
