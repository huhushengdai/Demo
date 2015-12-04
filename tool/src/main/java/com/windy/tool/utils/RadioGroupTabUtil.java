package com.windy.tool.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import java.util.ArrayList;

/**
 * RadioGroup + Fragment 显示方式切换的设置
 * 
 * @author Shrimp
 * @author Date: 15-02-26
 * @author Time: 20:00
 */
public class RadioGroupTabUtil implements OnCheckedChangeListener {
	ArrayList<Fragment> fragments;
	FragmentManager manager;
	RadioGroup rg;
	int resId;

    /**
     *
     *
     * @param fragments     数据，ArrayList<Fragment>
     * @param manager       管理员对象
     * @param rg            RadioGroup对象
     * @param resId         Fragment要显示的布局id
     * @param rb            首页显示的页面对应的按钮
     * @param index         首页显示的页面对应的标记
     */
	public RadioGroupTabUtil(ArrayList<Fragment> fragments,
			FragmentManager manager, RadioGroup rg, int resId, RadioButton rb,
			int index) {
		super();
		this.fragments = fragments;
		this.manager = manager;
		this.rg = rg;
		this.resId = resId;
		rb.setChecked(true);
		// 显示首页
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(resId, fragments.get(index));
		transaction.show(fragments.get(index));
		transaction.commit();
		// 设置监听
		rg.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		for (int i = 0; i < group.getChildCount(); i++) {
			Fragment fragment = fragments.get(i);
			if (checkedId == group.getChildAt(i).getId()) {
				addFragments(fragment);
			} else {
				FragmentTransaction transaction = manager.beginTransaction();
				transaction.hide(fragment);
				transaction.commit();
			}
		}
	}

	private void addFragments(Fragment fragment) {

		FragmentTransaction transaction = manager.beginTransaction();
		if (!fragment.isAdded()) {
			transaction.add(resId, fragment);
		}
		transaction.show(fragment);
		// 排队
		transaction.commit();
		// 插队
		// transaction.commitAllowingStateLoss();
	}
}
