package com.leqienglish;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.leqienglish.R;
import com.leqienglish.fragment.BooksPageFragment;
import com.leqienglish.fragment.FirstPageFragment;
import com.leqienglish.fragment.MyPageFragment;
import com.leqienglish.view.TabPagerAdapter;
import com.leqisoft.util.log.LOGGER;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {
    public final static LOGGER logger = new LOGGER(MainActivity.class);

    private Handler handler;

    /**
     * ViewPager对象的引用
     */
    private ViewPager mViewPager;
    private TabPagerAdapter tabPagerAdapter;


    /**
     * 装载Fragment的容器，我们的每一个界面都是一个Fragment
     */
    private List<Fragment> mFragmentList;

    private Button firstPageButton;
    private Button readingPageButton;
    private Button myPageButton;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.content_main);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        this.initFragment();

        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),
                mFragmentList);
        // 设置Adapter
        mViewPager.setAdapter(tabPagerAdapter);


        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        // 将Fragment加入到List中，并将Tab的title传递给Fragment


        tabPagerAdapter.notifyDataSetChanged();

    }


    private void initFragment() {
        mFragmentList = new ArrayList<Fragment>();
        this.firstPageButton = (Button) this.findViewById(R.id.mainPage);
        this.readingPageButton = (Button) this.findViewById(R.id.readingPage);
        this.myPageButton = (Button) this.findViewById(R.id.myPage);
        this.mFragmentList.add(new FirstPageFragment());
        this.mFragmentList.add(new BooksPageFragment());
        this.mFragmentList.add(new MyPageFragment());

    }


}
