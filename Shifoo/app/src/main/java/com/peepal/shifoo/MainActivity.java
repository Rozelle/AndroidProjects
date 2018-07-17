package com.peepal.shifoo;

import android.support.v7.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;
    MyDBHandler dbHandler;
    //public static ArrayList<String> phoneValueArr = new ArrayList<String>();
    //public static ArrayList<String> nameValueArr = new ArrayList<String>();

    //Action Bar
    private android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ViewPager and its adapters use support library fragments, so use getSupportFragmentManager.
        mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        if (position == 0 || position == 1 || position == 2)
                            getSupportActionBar().setSelectedNavigationItem(position);
                        mDemoCollectionPagerAdapter.notifyDataSetChanged();
                    }
                });

        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        // Initilization
        dbHandler = new MyDBHandler(this, null, null, 1);
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction){
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

            }
        };

        // Adding Tabs
        actionBar.addTab(actionBar.newTab()
                .setText("Students")
                .setTabListener((android.support.v7.app.ActionBar.TabListener) tabListener));
        actionBar.addTab(actionBar.newTab()
                .setText("Pending")
                .setTabListener((android.support.v7.app.ActionBar.TabListener) tabListener));
        actionBar.addTab(actionBar.newTab()
                .setText("Paid")
                .setTabListener((android.support.v7.app.ActionBar.TabListener) tabListener));

        actionBar.setHomeButtonEnabled(true);
        actionBar.setSelectedNavigationItem(0);
    }

    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment;
            if (i == 0)
                fragment = new StudentFragment();
            else
            if(i==1){
                fragment = new PendingFragment();
            }
            else
            fragment = new PaidFragment();
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            android.support.v7.app.ActionBar ab = getSupportActionBar();
            CharSequence tab = ab.getSelectedTab().getText();
            return tab;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


    /*public void buttonRequest(View v)
    {
        Intent i=new Intent(this,RequestPayment.class);
        startActivity(i);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                android.support.v7.app.ActionBar.Tab t = actionBar.getSelectedTab();
                String tab = t.getText().toString();
                Intent intent = new Intent(getApplicationContext(), SearchDisplay.class);
                intent.putExtra("tab", tab);
                intent.putExtra("query", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        // Configure the search info and add any event listeners
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //List all query items
                return true;  // Return true to expand action view
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        int id = item.getItemId();
        Intent i;
        switch (id) {
            case R.id.action_search:
                break;
            case R.id.overflow1:
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://advanced.shifoo.in/site/appterms-and-conditions"));
                startActivity(i);
                break;
            case R.id.overflow2:
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.shifoo.in/site/privacy-policy"));
                startActivity(i);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
