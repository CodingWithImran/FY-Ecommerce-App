package com.codingwithimran.fycommerce.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingwithimran.fycommerce.Adapters.OnBoardingAdapters;
import com.codingwithimran.fycommerce.R;

public class OnBoardingActivity extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout dotsLayout;
    OnBoardingAdapters onBoardingAdapters;
    Button btn, nextbtn;
    TextView[] dots;
    Animation animation;

    int mCurrentItem=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);
        btn = findViewById(R.id.get_started_btn);
        nextbtn = findViewById(R.id.next_btn);

        onBoardingAdapters = new OnBoardingAdapters(this);
        viewPager.setAdapter(onBoardingAdapters);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrentItem == 0){
                    mCurrentItem = 1;
                    viewPager.setCurrentItem(mCurrentItem);
                }
                else if(mCurrentItem == 1){
                    mCurrentItem= 2;
                    viewPager.setCurrentItem(mCurrentItem);
                }
                else{
                    mCurrentItem = 0;
                    viewPager.setCurrentItem(mCurrentItem);
                }
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnBoardingActivity.this, MainActivity.class));
                finish();
            }
        });
        adddots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }
    void adddots(int position){
        dots = new TextView[3];
        dotsLayout.removeAllViews();
        for(int i=0; i<dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html .fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dotsLayout.addView(dots[i]);
            if(dots.length > 0){
//                dots[position].setTextColor(Color.parseColor("#fad4d3"));
            }
        }

    }
    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
                    adddots(position);
                    if(position == 0 ){
                        btn.setVisibility(View.INVISIBLE);
                    } else if(position == 1){
                        btn.setVisibility(View.INVISIBLE);
                    }else{
                        animation = AnimationUtils.loadAnimation(OnBoardingActivity.this, R.anim.slide_animation);
                        btn.setAnimation(animation);
                        btn.setVisibility(btn.VISIBLE);
                    }
                }


        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}