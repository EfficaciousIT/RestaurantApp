package com.mobi.efficacious.esmartrestaurant.fragment;

import android.os.Bundle;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.activity.MainActivity;
import com.mobi.efficacious.esmartrestaurant.adapter.MyAdapter;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class main_fragment extends Fragment {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN = {R.drawable.chikendinner, R.drawable.pizzabanner};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    View myview;
    ImageView Starterimg, teabevergesimg, Pizzaimg, sandwichimg, juicebevergesimg, faloodaimg, riceimg;
    ImageView chineseimg, manicourseimg, continentalimg, soupimg, dessertsimg, icecremeimg;
String cat_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.main_layout, null);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Menu");
        Starterimg = (ImageView) myview.findViewById(R.id.img2);
        teabevergesimg = (ImageView) myview.findViewById(R.id.im2);
        Pizzaimg = (ImageView) myview.findViewById(R.id.im3);
        sandwichimg = (ImageView) myview.findViewById(R.id.img7);
        juicebevergesimg = (ImageView) myview.findViewById(R.id.juicebevergesimg);
        faloodaimg = (ImageView) myview.findViewById(R.id.img6);
        riceimg = (ImageView) myview.findViewById(R.id.img4);
        chineseimg = (ImageView) myview.findViewById(R.id.im4);
        manicourseimg = (ImageView) myview.findViewById(R.id.img9);
        continentalimg = (ImageView) myview.findViewById(R.id.continentalimg);
        soupimg = (ImageView) myview.findViewById(R.id.img10);
        dessertsimg = (ImageView) myview.findViewById(R.id.im5);
        icecremeimg = (ImageView) myview.findViewById(R.id.icecremeimg);
        Starterimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_id="15";
                menu(cat_id);
            }
        });
        teabevergesimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_id="6";
                menu(cat_id);
            }
        });
        Pizzaimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_id="4";
                menu(cat_id);
            }
        });
        sandwichimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_id="5";
                menu(cat_id);
            }
        });
        juicebevergesimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_id="11";
                menu(cat_id);
            }
        });
        faloodaimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_id="8";
                menu(cat_id);
            }
        });
        riceimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_id="20";
                menu(cat_id);
            }
        });
        chineseimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_id="18";
                menu(cat_id);
            }
        });
        manicourseimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_id="17";
                menu(cat_id);
            }
        });
        continentalimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_id="21";
                menu(cat_id);
            }
        });
        soupimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_id="14";
                menu(cat_id);
            }
        });
        dessertsimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_id="23";
                menu(cat_id);
            }
        });
        icecremeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_id="22";
                menu(cat_id);
            }
        });

        init();
        return myview;
    }

    public void menu(String Cat_id) {
        try
        {
            ViewMenuActivity viewMenuActivity = new ViewMenuActivity();
            Bundle args = new Bundle();
            args.putString("Cat_Id",Cat_id);
            viewMenuActivity.setArguments(args);
            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, viewMenuActivity).commitAllowingStateLoss();

        }catch (Exception  ex)
        {

        }

    }

    private void init() {
        for (int i = 0; i < XMEN.length; i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager) myview.findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(getActivity(), XMENArray));
        CircleIndicator indicator = (CircleIndicator) myview.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }
}
