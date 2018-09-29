package duti.com.droidtool.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import duti.com.droidtool.DroidTool;
import duti.com.droidtool.R;
import duti.com.droidtool.database.Repository;
import duti.com.droidtool.dtlib.ExceptionHandler;
import duti.com.droidtool.dtlib.SweetAlert;
import duti.com.droidtool.handlers.OnBoardHandler;
import duti.com.droidtool.model.crash.CrashInfo;
import duti.com.droidtool.services.CrashSyncService;

import static duti.com.droidtool.config.Constants.mItemName;
import static duti.com.droidtool.config.Constants.mTableName;


public abstract class BaseOnBoardingActivity extends AppCompatActivity {

    public TabLayout tabLayout;
    public ViewPager viewPager;
    public DrawerLayout drawerLayout;
    public TextView mNavHeaderTitleTV, mNavHeaderSubTitleTV, mNavFooterTV;
    public ActionBarDrawerToggle mDrawerToggle;
    public Toolbar mToolbar;
    public Context mContext;
    public DroidTool dt;
    public ActionBar actionBar;

    public void register(Context context, int resId, int activityTitle, int fontId){
        mContext = context;
        dt = new DroidTool(context);
        setupToolbar(resId, activityTitle, fontId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        new DroidTool(this).languageSettings.configLanguage();
        super.onCreate(savedInstanceState);

        mContext = this;
        dt = new DroidTool(this);

        // init crash detector
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
    }

    // bind toolbar
    public void setupToolbar(int resId, int titleResourceId, int fontId) {
        // Set up the toolbar.
        mToolbar = (Toolbar) findViewById(resId);
        setSupportActionBar(mToolbar);
        // set toolbar title
        actionBar = getSupportActionBar();
        if(fontId>0){
            dt.etc.setActionBarTitle(actionBar, dt.gStr(titleResourceId), dt.gStr(fontId));
        } else {
            dt.etc.setActionBarTitle(actionBar, dt.gStr(titleResourceId), dt.gStr(R.string.default_bangla_font));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Only handle with DrawerToggle if the drawer indicator is enabled.
        if (mDrawerToggle.isDrawerIndicatorEnabled() &&
                mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            // Handle home button in non-drawer mode
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // initialize tab layout with tab icon
    public void initTabs(int tabResId, int[] iconResId){
        tabLayout = (TabLayout) findViewById(tabResId);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.server_sync_tab_selector));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.register_tab_selector));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.data_summery_tab_selector));
        if( (iconResId != null) || (iconResId.length>0)){
            for(int i = 0; i<iconResId.length; i++){
                tabLayout.addTab(tabLayout.newTab().setIcon(iconResId[i]));
            }
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    // initialize view pager
    public void initViewPager(int resId, int pageLimit, int defaultPageNo){
        viewPager = (ViewPager) findViewById(resId);
        // for smooth transition between tabs
        viewPager.setOffscreenPageLimit(pageLimit);
        // initialize view pager adapter and setting that adapter
        final PagerAdapter adapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        // add tab layout listener into view pager listener
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // bind home tab as default tab when launch home fragment
        viewPager.setCurrentItem(defaultPageNo);

        // tab listener
        addTabsListener();
    }

    public interface onBoardListener {

        void onTabReady(ActionBar actionBar, int position);

        Fragment onTabSwitched(int position);

        void onDrawerMenuSelect(int menuId);

    }

    // tab listener
    public void addTabsListener(){
        // tab listener
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // bind the current item for which tab is selected
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // do something with only view pager listener
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // set specific toolbar title for page
                new OnBoardHandler(dt).onTabReady(actionBar, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    // initialize navigation drawer
    public void initNavigationDrawer(int navResId, int drawerResId, int titleRes, int subTitleRes, int footerRes) {

        NavigationView navigationView = (NavigationView) findViewById(navResId);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                // close drawer
                drawerLayout.closeDrawers();
                // set specific toolbar title for page
                new OnBoardHandler(dt).onDrawerMenuSelect(id);
                return true;
            }
        });

        // Click listener for nav footer.
        if(footerRes>0)mNavFooterTV = findViewById(footerRes);

        View header = navigationView.getHeaderView(0);
        mNavHeaderTitleTV = (TextView) header.findViewById(titleRes);
        if(subTitleRes>0)mNavHeaderSubTitleTV = (TextView) header.findViewById(subTitleRes);
        drawerLayout = (DrawerLayout) findViewById(drawerResId);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    public void setHeaderFooterContent(String title, String subTitle, String footerTitle){
        mNavHeaderTitleTV.setText(title);
        if(!TextUtils.isEmpty(subTitle)) mNavHeaderSubTitleTV.setText(subTitle);
        if(!TextUtils.isEmpty(footerTitle)) mNavFooterTV.setText(footerTitle);
    }

    @Override
    public void onBackPressed() {
        dt.alert.showWarning(dt.gStr(R.string.exit_message));
        dt.alert.setAlertListener(new SweetAlert.AlertListener() {
            @Override
            public void onAlertClick(boolean isCancel) {
                if (!isCancel) {
                    finish();
                }
            }
        });
    }

    // view pager adapter class to call different fragments
    class PageAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PageAdapter(FragmentManager fm, int numTabs) {
            super(fm);
            this.mNumOfTabs = numTabs;
        }

        @Override
        public Fragment getItem(int position) {
            // set specific fragment call for page
            return new OnBoardHandler(dt).onTabSwitched(position);
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // register run time internet checking broadcast receiver
        registerInternetCheckReceiver();
    }

    @Override
    public void onPause() {
        // unregister broadcast receiver
        unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    //Method to register runtime broadcast receiver to show internet connection status
    public void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, internetFilter);
    }

    //Runtime Broadcast receiver inner class to capture internet connectivity events
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = new DroidTool(mContext).droidNet.getConnectivityStatusString();
            if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
                if (!dt.tools.apiServiceRunning()) {
                    Repository<CrashInfo> repository = new Repository<CrashInfo>(mContext, new CrashInfo());
                    if(repository.getNotSyncDataCount()>0)
                        dt.activity.callService(CrashSyncService.class, mTableName, "CrashInfo", mItemName, dt.gStr(R.string.crash));
                }
            }
        }
    };

}

