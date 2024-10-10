package com.example.hodofiles;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.etsy.android.grid.util.DynamicHeightTextView;

/***
 * ADAPTER
 */

public class SearchFeedAdapter extends ArrayAdapter<String> {

    private static final String TAG = "SearchFeedAdapter";

    static class ViewHolder {
        DynamicHeightImageView imgLineOne;
        Button btnGo;
    }

    //private Context context;
    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private final ArrayList<Integer> samplePlaces;


    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public SearchFeedAdapter(final Context context, final int textViewResourceId) {
        super(context, textViewResourceId);
        //this.context= context;
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();

        samplePlaces = new ArrayList<Integer>();
        String imageName = "";
        String uri = "";
        String[] sample = context.getResources().getStringArray(R.array.sample_searchfeed);
        for (int i = 0; i < sample.length; i++) {
            imageName = sample[i];
            uri = "@drawable/" + imageName;
            int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
            //Drawable image = context.getResources().getDrawable(imageResource);
            samplePlaces.add(imageResource);
        }

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_sample, parent, false);
            vh = new ViewHolder();
            //vh.txtLineOne = (DynamicHeightTextView) convertView.findViewById(R.id.txt_line1);
            vh.imgLineOne = (DynamicHeightImageView) convertView.findViewById(R.id.img_line1);
            vh.btnGo = (Button) convertView.findViewById(R.id.btn_go);

            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);
        int backgroundIndex = position >= mBackgroundColors.size() ?
                position % mBackgroundColors.size() : position;

        int index = 0;

        //convertView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));
        //convertView.setBackgroundResource(samplePlaces.get(position));
        vh.imgLineOne.setImageResource(samplePlaces.get(position));

        Log.d(TAG, "getView position:" + position + " h:" + positionHeight);

        //vh.txtLineOne.setHeightRatio(positionHeight);
        //vh.txtLineOne.setText(getItem(position) + position);

        vh.imgLineOne.setHeightRatio(positionHeight);

        vh.btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(getContext(), "Button Clicked Position " +
                        position, Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }

}