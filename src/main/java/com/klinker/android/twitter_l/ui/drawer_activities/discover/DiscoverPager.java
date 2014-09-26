package com.klinker.android.twitter_l.ui.drawer_activities.discover;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.astuetz.PagerSlidingTabStrip;
import com.klinker.android.twitter_l.R;
import com.klinker.android.twitter_l.adapters.TrendsPagerAdapter;
import com.klinker.android.twitter_l.manipulations.widgets.HoloEditText;
import com.klinker.android.twitter_l.settings.AppSettings;
import com.klinker.android.twitter_l.settings.PrefActivity;
import com.klinker.android.twitter_l.ui.setup.LoginActivity;
import com.klinker.android.twitter_l.ui.drawer_activities.DrawerActivity;
import com.klinker.android.twitter_l.utils.Utils;

/**
 * Created by luke on 11/29/13.
 */
public class DiscoverPager extends DrawerActivity {

    private TrendsPagerAdapter mSectionsPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        sharedPrefs = context.getSharedPreferences("com.klinker.android.twitter_world_preferences",
                Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
        settings = AppSettings.getInstance(this);

        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        setUpTheme();
        setContentView(R.layout.trends_activity);
        setUpDrawer(3, getResources().getString(R.string.trends));

        actionBar = getActionBar();
        actionBar.setTitle(getResources().getString(R.string.trends));


        if (!settings.isTwitterLoggedIn) {
            Intent login = new Intent(context, LoginActivity.class);
            startActivity(login);
            finish();
        }

        mSectionsPagerAdapter = new TrendsPagerAdapter(getFragmentManager(), context);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOverScrollMode(ViewPager.OVER_SCROLL_NEVER);

        PagerSlidingTabStrip strip = (PagerSlidingTabStrip) findViewById(R.id.pager_tab_strip);
        //PagerTitleStrip strip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
        strip.setBackgroundColor(settings.themeColors.primaryColor);
        strip.setTextColorResource(R.color.white);
        strip.setIndicatorColor(settings.themeColors.accentColor);
        strip.setTextSize((int)getResources().getDimension(R.dimen.pager_tab_strip_text));
        strip.setViewPager(mViewPager);

        if (getResources().getBoolean(R.bool.has_drawer)) {
            mViewPager.setTranslationY(Utils.getStatusBarHeight(this));
            strip.setTranslationY(Utils.getStatusBarHeight(this));
        }

        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            mDrawerToggle.onConfigurationChanged(newConfig);
        } catch (Exception e) { }

        overridePendingTransition(0,0);
        finish();
        Intent restart = new Intent(context, DiscoverPager.class);
        restart.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(0, 0);
        startActivity(restart);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trends_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        final int DISMISS = 0;
        final int SEARCH = 1;
        final int COMPOSE = 2;
        final int NOTIFICATIONS = 3;
        final int DM = 4;
        final int SETTINGS = 5;
        final int TOFIRST = 6;

        menu.getItem(NOTIFICATIONS).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
        } catch (Exception e) {

        }

        switch (item.getItemId()) {
            case R.id.location_settings:
                Intent settings = new Intent(context, PrefActivity.class);
                settings.putExtra("position", 9)
                        .putExtra("title",
                                getResources().getString(R.string.location_settings));
                finish();
                settings.putExtra("open_help", true);
                startActivity(settings);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
