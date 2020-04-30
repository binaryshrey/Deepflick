package com.example.deepflick.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepflick.R;
import com.example.deepflick.model.Review;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder>{

    //private Review[] mReviewData;
    private ArrayList<Review> mReviewData;
    TextView mAuthor;
    TextView mContent;

    public ReviewsAdapter(ArrayList<Review> review) {
        mReviewData = review;
    }

    @NonNull
    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        //inflate list item xml into a view
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapterViewHolder holder, int position) {
        String AuthorToBind = mReviewData.get(position).getAuthor();
        String ReviewToBind = mReviewData.get(position).getContent();
        mAuthor.setText(AuthorToBind);
        mContent.setText(ReviewToBind);
    }

    @Override
    public int getItemCount() {
        if (mReviewData == null) {
            return 0;
        }
        return mReviewData.size();
    }

    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder {
        public ReviewsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mAuthor = itemView.findViewById(R.id.author);
            mContent = itemView.findViewById(R.id.content);
        }
    }
}
