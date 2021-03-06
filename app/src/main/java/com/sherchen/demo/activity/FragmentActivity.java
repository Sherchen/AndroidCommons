package com.sherchen.demo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.sherchen.demo.R;
import com.sherchen.common.util.FragmentUtils;
import com.sherchen.demo.fragment.Demo0Fragment;

import java.util.ArrayList;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 17/02/01
 *     desc  :
 * </pre>
 */
public class FragmentActivity extends AppCompatActivity {

    public Fragment rootFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(Demo0Fragment.newInstance());
        rootFragment = FragmentUtils.addFragments(getSupportFragmentManager(), fragments, R.id.fragment_container, 0);
    }


    @Override
    public void onBackPressed() {
        if (!FragmentUtils.dispatchBackPress(getSupportFragmentManager())) {
            super.onBackPressed();
        }
    }
}