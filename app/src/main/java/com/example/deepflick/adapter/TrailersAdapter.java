package com.example.deepflick.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepflick.R;
import com.example.deepflick.model.Trailer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewModel> {

    //declaring data members
    TextView mTrailerName;
    private Trailer[] mTrailerData;
    final String BASE_URL = "https://www.youtube.com/watch?v=";
    Context context;

    //constructor
    public TrailersAdapter(Trailer[] trailer, Context context) {
        mTrailerData = trailer;
        this.context = context;
    }


    //methods onCreateViewHolder, onBindViewHolder, getItemCount
    @NonNull
    @Override
    public TrailersAdapterViewModel onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        //inflate list item xml into a view
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailersAdapterViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersAdapterViewModel holder, int position) {
        String TrailerToBind = mTrailerData[position].getName();
        final String TrailerToWatch = mTrailerData[position].getKey();
        mTrailerName.setText(TrailerToBind);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //click listener
                Uri openTrailerVideo = Uri.parse(BASE_URL + TrailerToWatch);
                Intent intent = new Intent(Intent.ACTION_VIEW, openTrailerVideo);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        //returning the count of mTrailerData
        if (mTrailerData == null) {
            return 0;
        }
        return mTrailerData.length;
    }

    public class TrailersAdapterViewModel extends RecyclerView.ViewHolder{
        //using @BindView along with the id of the view to declare view variable
        @BindView(R.id.Trailer_name)
        TextView mTrailerName;

        public TrailersAdapterViewModel(@NonNull View itemView) {
            super(itemView);
            //binding the view using butterknife
            ButterKnife.bind(this,itemView);
        }
    }
}
