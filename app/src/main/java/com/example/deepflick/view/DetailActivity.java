package com.example.deepflick.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deepflick.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    //using @BindView along with the id of the view to declare view variable
    @BindView(R.id.detail_thumbnail)
    ImageView mDetailThumbnail;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.rating)
    TextView mRating;

    @BindView(R.id.release_date)
    TextView mReleaseDate;

    @BindView(R.id.adult)
    TextView mAdult;

    @BindView(R.id.overview)
    TextView mOverview;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting contentView layout
        setContentView(R.layout.activity_detail);
        //binding the view using butterknife
        ButterKnife.bind(this);

        //loading image with picasso into mDetailThumbnail view
        Picasso.get()
                .load(getIntent().getStringExtra("poster"))
                .into(mDetailThumbnail);

        //setting the title of the activity as movie title
        setTitle(getIntent().getStringExtra("title"));
        //getting values from intent and setting it in their respective views
        mTitle.setText(getIntent().getStringExtra("title"));
        mRating.append(getIntent().getStringExtra("rate"));
        mReleaseDate.append(getIntent().getStringExtra("release"));
        mOverview.setText(getIntent().getStringExtra("overview"));
        //storing the value of adult content in a variable
        String adult = getIntent().getStringExtra("adult");
        //rendering the value based on given condition
        if(adult.equals("true"))
            mAdult.setText("Yes");
        else
            mAdult.setText("No");

    }
}