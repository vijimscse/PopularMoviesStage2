package com.udacity.popularmoviesstage2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.popularmoviesstage2.R;
import com.udacity.popularmoviesstage2.dto.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by VijayaLakshmi.IN on 7/14/2017.
 */

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ReviewViewHolder> {

    private final Context mContext;
    private final List<Review> mReviewList;

    public ReviewRecyclerViewAdapter(Context context, List<Review> reviewList) {
        mContext = context;
        mReviewList = reviewList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_row, parent, false);

        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder reviewViewHolder, int position) {
        Review review = mReviewList.get(position);

        reviewViewHolder.mTitle.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        int count = 0;

        if (mReviewList != null) {
            count = mReviewList.size();
        }

        return count;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.reviews_title)
        TextView mTitle;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
