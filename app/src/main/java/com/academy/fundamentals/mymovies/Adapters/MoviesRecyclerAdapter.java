package com.academy.fundamentals.mymovies.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.academy.fundamentals.mymovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MoviesRecyclerAdapterViewHolder> {
    private final MoviesAdapterOnClickHandler mClickHandler;
    private static final int CARDS_COUNT = 5;

    public interface MoviesAdapterOnClickHandler {
        void onClick(int movieIndex);
    }

    public MoviesRecyclerAdapter(MoviesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    class MoviesRecyclerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.coverIV) ImageView coverIV;
        @BindView(R.id.titleTV) TextView titleTV;
        @BindView(R.id.overviewTV) TextView overviewTV;

        public MoviesRecyclerAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int movieIndex = getAdapterPosition();
            mClickHandler.onClick(movieIndex);
        }
    }

    @Override
    public int getItemCount() {
        return CARDS_COUNT;
    }

    @NonNull
    @Override
    public MoviesRecyclerAdapterViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        int layoutId = R.layout.item_movie_result;

        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(layoutId, parent, false);

        return new MoviesRecyclerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesRecyclerAdapterViewHolder moviesRecyclerAdapterViewHolder, int position) {
        switch (position){
            case 0:
                moviesRecyclerAdapterViewHolder.coverIV.setBackgroundResource(R.drawable.bp);
                break;
            case 1:
                moviesRecyclerAdapterViewHolder.coverIV.setBackgroundResource(R.drawable.d2);
                break;
            case 2:
                moviesRecyclerAdapterViewHolder.coverIV.setBackgroundResource(R.drawable.jw);
                break;
            case 3:
                moviesRecyclerAdapterViewHolder.coverIV.setBackgroundResource(R.drawable.tfp);
                break;
            case 4:
                moviesRecyclerAdapterViewHolder.coverIV.setBackgroundResource(R.drawable.tm);
                break;
        }
        moviesRecyclerAdapterViewHolder.titleTV.setText("Test " + position);
        moviesRecyclerAdapterViewHolder.overviewTV.setText("Overview " + position);
    }
}