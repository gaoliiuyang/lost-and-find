package com.gly.collagelf.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
/**
 * FragmentPagerAdapter
 * @author ∏ﬂ¡Ù—Û
 *
 */
public class FragmentViewPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;
	public FragmentViewPagerAdapter(FragmentManager manager,List<Fragment> fragments) {
		super(manager);
		this.fragments = fragments;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	
}
