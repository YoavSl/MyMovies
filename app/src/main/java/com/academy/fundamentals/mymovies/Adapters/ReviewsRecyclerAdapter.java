package com.academy.fundamentals.mymovies.Adapters;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.academy.fundamentals.mymovies.Models.Review;
import com.academy.fundamentals.mymovies.R;

import java.util.List;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ReviewsRecyclerAdapter extends RecyclerView.Adapter<ReviewsRecyclerAdapter.ReviewsRecyclerAdapterViewHolder> {
    private List<Review> reviews;

    public ReviewsRecyclerAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewsRecyclerAdapterViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        int layoutId = R.layout.recycler_item_review;

        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(layoutId, parent, false);

        return new ReviewsRecyclerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsRecyclerAdapterViewHolder reviewsRecyclerAdapterViewHolder, int position) {
        reviewsRecyclerAdapterViewHolder.bind(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ReviewsRecyclerAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.authorTV) TextView authorTV;
        @BindView(R.id.contentETV) ExpandableTextView contentETV;

        private ReviewsRecyclerAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        private void bind(Review review) {
            authorTV.setText(review.getAuthor());
            contentETV.setText(review.getContent());
        }

        @OnClick(R.id.contentETV)
        void OnClick(View view) {
            contentETV.toggle();
        }
    }
}