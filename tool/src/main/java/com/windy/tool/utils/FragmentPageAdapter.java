package com.windy.tool.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @Package com.qiafeng.httputilsdemo.adapter
 * @作 用:
 * @创 建 人: zhangwei
 * @日 期: 15/3/11
 * @修 改 人:
 * @日 期:
 */
public class FragmentPageAdapter extends PagerAdapter {
    private List<Fragment> fragments;
    private FragmentManager manager;

    public FragmentPageAdapter(List<Fragment> fragments, FragmentManager manager) {
        this.fragments = fragments;
        this.manager = manager;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = fragments.get(position);
        if (!fragment.isAdded()) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(fragment, fragment.getClass().getName());
            //插队
            transaction.commitAllowingStateLoss();
            manager.executePendingTransactions();
        }
        if (fragment.getView().getParent() == null) {
            container.addView(fragment.getView());
        }
        return fragment.getView();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(fragments.get(position).getView());
    }
}
