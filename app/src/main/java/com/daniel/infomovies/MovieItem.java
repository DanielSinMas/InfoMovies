package com.daniel.infomovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Daniel on 15/02/2017.
 */

public class MovieItem implements Parcelable {

    String id;
    String title;
    String poster_path;
    String synopsis;
    float vote_average;
    String release_date;


    public MovieItem(String id, String title, String poster_path, String synopsis, float vote_average, String release_date){
        this.id=id;
        this.title=title;
        this.poster_path=poster_path;
        this.synopsis=synopsis;
        this.vote_average=vote_average;
        this.release_date=release_date;
    }

    protected MovieItem(Parcel in) {
        id = in.readString();
        title = in.readString();
        poster_path = in.readString();
        synopsis = in.readString();
        vote_average = in.readFloat();
        release_date = in.readString();
    }

    public void setId(String ident){
        this.id=ident;
    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeString(synopsis);
        dest.writeFloat(vote_average);
        dest.writeString(release_date);
    }
}