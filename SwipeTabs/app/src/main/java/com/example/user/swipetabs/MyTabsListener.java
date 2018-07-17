package com.example.user.swipetabs;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by User on 25-06-2015.
 */
public class MyTabsListener<T extends Fragment>  implements ActionBar.TabListener{
    //Context context;
    private Fragment mFragment;
    private final Activity mActivity;
    private final String mTag;
    private final Class<T> mClass;

    MyTabsListener(Activity activity, String tag, Class<T> clz) {
        mActivity = activity;
        mTag = tag;
        mClass = clz;
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        //Toast.makeText(context,tab.getText()+" Selected",Toast.LENGTH_SHORT).show();
        // Check if the fragment is already initialized

        if (mFragment == null) {
            // If not, instantiate and add it to the activity
            mFragment = Fragment.instantiate(mActivity, mClass.getName());
            fragmentTransaction.add(android.R.id.content, mFragment, mTag);
        } else {
            // If it exists, simply attach it in order to show it
            fragmentTransaction.attach(mFragment);
        }

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        //Toast.makeText(context,tab.getText()+" Unselected",Toast.LENGTH_SHORT).show();
        if (mFragment != null) {
            // Detach the fragment, because another one is being attached
            fragmentTransaction.detach(mFragment);
        }

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }
}
