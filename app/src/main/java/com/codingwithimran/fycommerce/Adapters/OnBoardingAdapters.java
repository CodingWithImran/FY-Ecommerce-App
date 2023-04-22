package com.codingwithimran.fycommerce.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.codingwithimran.fycommerce.R;

public class OnBoardingAdapters extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    public OnBoardingAdapters(Context context) {
        this.context = context;
    }
     int ImgArray[] = {
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3
    };
     int headingArray[] = {
            R.string.first_slide,
            R.string.second_slide,
            R.string.third_slide
    };
    int descriptionArray[] = {
            R.string.description,
            R.string.description,
            R.string.description
    };
    @Override
    public int getCount() {
        return headingArray.length;
    }



    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.sliding_layout, container, false);

        ImageView imageView = view.findViewById(R.id.slider_img);
        TextView title = view.findViewById(R.id.heading);
        TextView description = view.findViewById(R.id.description);
        imageView.setImageResource(ImgArray[position]);
        title.setText(headingArray[position]);
        description.setText(descriptionArray[position]);

        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (ConstraintLayout) object;
        container.removeView(view);
    }
}
